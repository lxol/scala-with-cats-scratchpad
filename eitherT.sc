// Chapter 5

import cats.Monad
import cats.syntax.applicative._
import cats.syntax.flatMap._
import scala.language.higherKinds

// Hypothetical example. This won't actually compile:
interp.configureCompiler(_.settings.fatalWarnings.value = true)
interp.configureCompiler(_.settings.YpartialUnification.value = true)

import $ivy.`org.typelevel::cats-core:1.0.0`

import $ivy.`com.typesafe.play::play-json:2.6.0`
import $ivy.`com.typesafe.play::play-json:2.6.0`

import $ivy.`org.scalacheck::scalacheck:1.14.0`
import $ivy.`org.typelevel::simulacrum:1.0.0`
import $plugin.$ivy.`org.typelevel::kind-projector:0.10.0`

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

// try to find the value it wraps
// and it will give you an idea of what it wraps
case class MyEitherT[F[_], L, R](value: F[Either[L, R]])(implicit F: Monad[F]) {
  def map[S](f: R => S): MyEitherT[F, L, S] =
    MyEitherT(F.map(value)(e => e.right.map(f)))

  def flatMap[S](f: R => MyEitherT[F, L, S]): MyEitherT[F, L, S] =
    F.flatMap(value)(e /* Either[L, R] */ => e match {
      case Left()   =>  MyEitherT(F.pure(l))
      case Right(r) =>  ???
    }
    /* */)
}

object MyEitherT {
  // why do we want a Functor instance if we have a MyEitherT.
  // 1 . Because the code that code can works generically  ???
  // 2 . We can compose with others ???
  implicit def functorInstance[F[_]: Functor, L]: Functor[MyEitherT[F, L, ?]] =
    new Functor[MyEitherT[F, L, ?]] {
      def map[A, B](fa: MyEitherT[F, L, A])(f: A => B): MyEitherT[F, L, B] =
        fa.map(f)
    }

  implicit def monadInstance[F[_], L](implicit F: Monad[F]): Monad[MyEitherT[F, L, ?]] =
    new Monad[MyEitherT[F, L, ?]] {
      def pure[A](x: A): MyEitherT[F, L, A] = MyEitherT(F.pure(Right(x)))

     //def tailRecM[A, B](a: A)(f: A => Composed[Either[A,B]]): Composed[B] = ???
    }
}

//
{
  sealed trait Widget extends Serializable with Product
  case class Sprocket(x: Int) extends Widget
  case class Woozle(s: String) extends Widget
  val x = List(Sprocket(1))
}
