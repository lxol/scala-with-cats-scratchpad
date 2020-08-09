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

// 4.10 Defining Custom Monads

import cats.Monad

import scala.annotation.tailrec

// val optionMonad = new Monad[Option] {
//   def flatMap[A, B](opt: Option[A])(fn: A => Option[B]): Option[B] =
//     opt flatMap fn

//   def pure[A](opt: A): Option[A] = Some(opt)

//   @tailrec
//   def tailRecM[A, B](a: A)(fn: A => Option[Either[A, B]]): Option[B] =
//     fn(a) match {
//       case None           => None
//       case Some(Left(a1)) => tailRecM(a1)(fn)
//       case Some(Right(b)) => Some(b)
//     }
// }

// trait Foo[A]

// val fooMonad = new Monad[Foo] {
//   def flatMap[A, B](opt: Foo[A])(fn: A => Foo[B]): Foo[B] = new Foo[B] {}

//   def pure[A](opt: A): Foo[A] = new Foo[A] {}

//   def tailRecM[A, B](a: A)(fn: A => Foo[Either[A, B]]): Foo[B] = new Foo[B] {}
// }

// sealed trait Tree[+A]

// final case class Branch[A](left: Tree[A], right: Tree[A]) extends Tree[A]

// final case class Leaf[A](value: A) extends Tree[A]

// def branch[A](left: Tree[A], right: Tree[A]): Tree[A] = Branch(left, right)

// def leaf[A](value: A): Tree[A] = Leaf(value)

// val treeMonad = new Monad[Tree] {

//   def flatMap[A, B](tree: Tree[A])(fn: A => Tree[B]): Tree[B] =
//     tree match {
//       case Leaf(value)         => fn(value)
//       case Branch(left, right) => branch(flatMap(left)(fn), flatMap(right)(fn))
//     }

//   def pure[A](value: A): Tree[A] = leaf(value)

//   //@tailrec
//   def tailRecM[A, B](a: A)(fn: A => Tree[Either[A, B]]): Tree[B] =
//     flatMap(fn(a)) {
//       case Left(value)  => tailRecM(value)(fn)
//       case Right(value) => leaf(value)
//     }
  // def tailRecM[A, B](a: A)(fn: A => Tree[Either[A, B]]): Tree[B] = {

  //   def aux(tree: Tree[Either[A, B]]): Tree[B] =
  //     tree match {
  //       case Leaf(Left(value))   => tailRecM(value)(fn)
  //       case Leaf(Right(value))  => leaf(value)
  //       case Branch(left, right) => branch(aux(left), aux(right))
  //     }
  //   aux(fn(a))
  // }
//

// Monad Transformer

// def lookupUser(id: Long) = ???
// def lookupUserName(id: Long): Either[Error, Option[String]] =
//   for {
//     optUser <- lookupUser(id)
//   } yield {
//     for { user <- optUser } yield user.name
//   }
