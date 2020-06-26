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
import scala.concurrent.{Future, Await}
import scala.concurrent.ExecutionContext.Implicits.global

import scala.concurrent.duration._

import cats._ 
import cats.instances.all._ 
import cats.syntax.all._ 
import cats.implicits._ 
import org.scalacheck.Properties
import org.scalacheck.Prop.forAll

import simulacrum._

// case class Foo(value: Monad[_])
// example List[Option[_]]
// def compose[M1[_]: Monad, M2[_]: Monad] = {

//   type Composed[A] = M1[M2[A]]

//  new Monad[Composed] {

//     def pure[A](a: A): Composed[A] = a.pure[M2].pure[M1]

//     def flatMap[A, B](fa: Composed[A])(f: A => Composed[B]): Composed[B] =  
//   }
// }






















// def compose[M1[_], M2[_]](implicit M1: Monad[M1], M2: Monad[M2]) = {

//   type Composed[A] = M1[M2[A]]

//   new Monad[Composed] {
//     def pure[A](a: A): Composed[A] = M1.pure(M2.pure(a))

//     def flatMap[A, B](fa: Composed[A])(f: A => Composed[B]): Composed[B] = 
//       M1.flatMap(fa)((xm2: M2[A]) => M2.flatMap(xm2)(a => ???))

//     def tailRecM[A, B](a: A)(f: A => Composed[Either[A,B]]): Composed[B] = ???
//   }
// }
