interp.configureCompiler(_.settings.fatalWarnings.value = true)
interp.configureCompiler(_.settings.YpartialUnification.value = true)

import $ivy.`org.typelevel::cats-core:1.0.0`

import $ivy.`com.typesafe.play::play-json:2.6.0`
import $ivy.`com.typesafe.play::play-json:2.6.0`

import $ivy.`org.scalacheck::scalacheck:1.14.0`

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

//val p = Prop.forAll((x:Int) => (x + 1 == x +  1))
// p.check
// def asssocCheck[F[_], A](implicit F: Monad[F], arbFA: Arbitrary[F[A]], arbA: Arbitrary[A] )  = Prop.forAll((m:F[A]) => F.flatMap(m)(x => F.pure(x) ) == m )

case class Foo(a: String)

for {
  a <- List(1)
} yield 5

Monad[List].flatMap(x => ???)

def lookupUser(id: Long): Future[String] = ???

def lookupUserName(id: Long): Either[Error, Option[String]] =
  for {
    optUser <- lookupUser(id)
  } yield {
    for { user <- optUser } yield user.name
  }
