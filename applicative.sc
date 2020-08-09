import $ivy.`org.typelevel::cats-core:2.0.0`
import $ivy.`org.scalacheck::scalacheck:1.14.0`
import $ivy.`org.typelevel::simulacrum:1.0.0`
import $plugin.$ivy.`org.typelevel:::kind-projector:0.11.0`

import cats._
import cats.implicits._
import cats.instances.all._
import cats.syntax.all._
import org.scalacheck.Prop.forAll
import org.scalacheck.Properties
import scala.concurrent.ExecutionContext
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{ Await, Future }
import simulacrum._

import cats.Monad

import scala.annotation.tailrec

trait MyApplicative[F[_]] extends Functor[F] { self => 
  def pure[A](a: A): F[A]
  def apply[A, B](fa: F[A])(ff: F[A => B]): F[B]

  def apply2[A, B, Z](fa: F[A], fb: F[B])(ff: F[(A, B) => Z]): F[Z] =
    apply(fa)(apply(fb)(map(ff)(f => b => a => f(a, b))))

  def apply3[A, B, C, Z](fa: F[A], fb: F[B], fc: F[C])(ff: F[(A, B, C) => Z]): F[Z] =
    apply(fa)(apply2(fb, fc)(map(ff)(f => (b, c) => a => f(a, b, c))))

  override def map[A, B](fa: F[A])(f: A => B): F[B] =
    apply(fa)(pure(f))

  def map2[A, B, Z](fa: F[A], fb: F[B])(f: (A, B) => Z): F[Z] =
    apply(fa)(map(fb)(b => a => f(a, b)))

  def map3[A, B, C, Z](fa: F[A], fb: F[B], fc: F[C])(f: (A, B, C) => Z): F[Z] =
    apply(fc)(map2(fa, fb)((a, b) => c => f(a, b, c)))

  // def map4[A, B, C, D, Z](fa: F[A], fb: F[B], fc: F[C], fd: F[D])(f: (A, B, C, D) => Z): F[Z] =
  //   apply(fa)(map3(fb, fc, fd)((b, c, d) => a => f(a, b, c, d)))

  def map4[A, B, C, D, Z](fa: F[A], fb: F[B], fc: F[C], fd: F[D])(f: (A, B, C, D) => Z): F[Z] =
    // map2(tuple2(fa, fb), tuple2(fc, fd)){case ( x, y) => f(x._1,x._2, y._1, y._2)}
    map2(tuple2(fa, fb), tuple2(fc, fd)) { case ((a, b), (c, d)) => f(a, b, c, d) }

  def tuple2[A, B](fa: F[A], fb: F[B]): F[(A, B)] = map2(fa, fb)((a, b) => (a, b))

  def compose[G[_]](implicit G: Applicative[G]): MyApplicative[Lambda[X => F[G[X]]]] =
    new MyApplicative[Lambda[X => F[G[X]]]] {

      def pure[A](a: A): F[G[A]] = self.pure(G.pure(a))

      def apply[A, B](fa: F[G[A]])(ff: F[G[A => B]]): F[G[B]] =  {
        val x: F[G[A]] => G[B] = ???
        ???
      }

    }
  def flip[A,B](ff: F[A => B]): F[A] => F[B] = fa => apply(fa)(ff)
}

object MyApplicative {
  implicit val optionApplicative: MyApplicative[Option] = new MyApplicative[Option] {
    def pure[A](a: A): Option[A] = Some(a)
    def apply[A, B](fa: Option[A])(ff: Option[A => B]): Option[B] =
      (fa, ff) match {
        case (None, _)          => None
        case (Some(a), None)    => None
        case (Some(a), Some(f)) => Some(f(a))
      }
  }
  implicit val listApplicative: MyApplicative[List] = new MyApplicative[List] {
    def pure[A](a: A): List[A] = List(a)
    def apply[A, B](fa: List[A])(ff: List[A => B]): List[B] =
      for {
        a <- fa
        f <- ff
      } yield f(a)

  }

}
val optionApplicative: MyApplicative[Option] = new MyApplicative[Option] {
  def pure[A](a: A): Option[A] = Some(a)
  def apply[A, B](fa: Option[A])(ff: Option[A => B]): Option[B] =
    (fa, ff) match {
      case (None, _)          => None
      case (Some(a), None)    => None
      case (Some(a), Some(f)) => Some(f(a))
    }
}
