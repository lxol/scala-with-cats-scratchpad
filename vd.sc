//interp.configureCompiler(_.settings.Werror.value = true)
// interp.configureCompiler(_.settings.XfatalWarnings.value = true)
//interp.configureCompiler(_.settings.Ypartial-unification.value = true)

import $ivy.`org.typelevel::cats-core:2.0.0`
import $ivy.`org.typelevel::cats-free:2.0.0`

// import $ivy.`com.typesafe.play::play-json:2.6.0`
// import $ivy.`com.typesafe.play::play-json:2.6.0`

import $ivy.`org.scalacheck::scalacheck:1.14.0`
import $ivy.`org.typelevel::simulacrum:1.0.0`
import $plugin.$ivy.`org.typelevel:::kind-projector:0.11.0`

import scala.concurrent.ExecutionContext
import scala.concurrent.{ Await, Future }
import scala.concurrent.ExecutionContext.Implicits.global

import scala.concurrent.duration._

import cats._
import cats.instances.all._
import cats.syntax.all._
import cats.implicits._
import org.scalacheck.Properties
import org.scalacheck.Prop.forAll

import simulacrum._

import cats.free.Free
sealed trait KVStoreA[A]
case class Put[T](key: String, value: T) extends KVStoreA[Unit]
case class Get[T](key: String) extends KVStoreA[Option[T]]
case class Delete[T](key: String) extends KVStoreA[Unit]

import cats.free.Free
type KVStore[A] = Free[KVStoreA, A]

import cats.free.Free.liftF

def put[T](key: String, value: T): KVStore[Unit] =
  liftF[KVStoreA, Unit](Put[T](key, value))

def get[T](key: String): KVStore[Option[T]] =
  liftF[KVStoreA, Option[T]](Get[T](key))

def delete(key: String): KVStore[Unit] =
  liftF[KVStoreA, Unit](Delete(key))

def update[T](key: String, f: T => T): KVStore[Unit] =
  for {
    vMaybe <- get[T](key)
    _      <- vMaybe.map(v => put[T](key, f(v))).getOrElse(Free.pure())
  } yield ()

def program: KVStore[Option[Int]] =
  for {
    _ <- put("wild-cats", 2)
    _ <- update[Int]("wild-cats", (_ + 12))
    _ <- put("tame-cats", 5)
    n <- get[Int]("wild-cats")
    _ <- delete("tame-cats")
  } yield n

import cats.arrow.FunctionK
import cats.{ Id, ~> }
import scala.collection.mutable

def impureCompiler: KVStoreA ~> Id =
  new (KVStoreA ~> Id) {
    val kvs = mutable.Map.empty[String, Any]
    def apply[A](fa: KVStoreA[A]): Id[A] =
      fa match {
        case Put(key, value) =>
          println(s"put($key, $value)")
          kvs(key) = value
          ()
        case Get(key) =>
          println(s"get($key)")
          kvs.get(key).map(_.asInstanceOf[A])
        case Delete(key) =>
          println(s"delete($key)")
          kvs.remove(key)
          ()

      }
  }

val result: Option[Int] = program.foldMap(impureCompiler)

import cats.data.State

type KVStoreState[A] = State[Map[String, Any], A]

val pureCompiler: KVStoreA ~> KVStoreState =
  new (KVStoreA ~> KVStoreState) {
    def apply[A](fa: KVStoreA[A]): KVStoreState[A] =
      fa match {
        case Put(key, value) => State.modify(_.updated(key, value))
        case Get(key)        => State.inspect(_.get(key).map(_.asInstanceOf[A]))
        case Delete(key)     => State.modify(_ - key)
      }
  }

val resultPure: (Map[String, Any], Option[Int]) = program
  .foldMap(pureCompiler)
  .run(Map.empty)
  .value

import cats.data.EitherK
import cats.free.Free
import scala.collection.mutable.ListBuffer

// Handles user interaction

sealed trait Interact[A]
case class Ask(prompt: String) extends Interact[String]
case class Tell(msg: String) extends Interact[Unit]

sealed trait DataOp[A]
case class AddCat(a: String) extends DataOp[Unit]
case class GetAllCats() extends DataOp[List[String]]

type CatsApp[A] = EitherK[DataOp, Interact, A]

// Lift our Algebra to Free Monads
class Interacts[F[_]](implicit I: InjectK[Interact, F]) {
  def tell(msg: String): Free[F, Unit] = Free.inject[Interact, F](Tell(msg))
  def ask(prompt: String): Free[F, String] = Free.inject[Interact, F](Ask(prompt))
}

object Interacts {
  implicit def iteracts[F[_]](implicit I: InjectK[Interact, F]): Interacts[F] = new Interacts[F]
}

class DataSource[F[_]](implicit I: InjectK[DataOp, F]) {
  def addCat(a: String): Free[F, Unit] = Free.inject[DataOp, F](AddCat(a))
  def getAllCats: Free[F, List[String]] = Free.inject[DataOp, F](GetAllCats())
}

object DataSource {
  implicit def dataSource[F[_]](implicit I: InjectK[DataOp, F]): DataSource[F] =
    new DataSource[F]
}

def programC(implicit I: Interacts[CatsApp], D: DataSource[CatsApp]): Free[CatsApp, Unit] = {
  import I._, D._
  for {
    cat  <- ask("What's the kitty's name")
    _    <- addCat(cat)
    cats <- getAllCats
    _    <- tell(cats.toString)

  } yield ()
}

object ConsoleCatsInterpreter extends (Interact ~> Id) {
  def apply[A](i: Interact[A]) = i match {
    case Ask(prompt) =>
      println(prompt)
      scala.io.StdIn.readLine()
    case Tell(msg) =>
      println(msg)
  }
}
object InMemoryDataSourceInterpreter extends (DataOp ~> Id) {
  private[this] val memDataSet = new ListBuffer[String]
  def apply[A](fa: DataOp[A]) = fa match {
    case AddCat(a)     => memDataSet.append(a); ()
    case GetAllCats() => memDataSet.toList
  }
}

val interpreter: CatsApp ~> Id = InMemoryDataSourceInterpreter or ConsoleCatsInterpreter

import DataSource._, Interacts._

val evaled: Unit = programC.foldMap(interpreter)
