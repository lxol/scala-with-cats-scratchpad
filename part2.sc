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
import scala.concurrent.duration._
import simulacrum._
import scala.concurrent.{ Await, Future }

import cats.Monad

import scala.annotation.tailrec

import scala.concurrent._, duration.Duration.Inf, java.util.concurrent.Executors 

implicit val ec = ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(100))

// visual vm
// def myDistributedMap[A, B](xs:List[A])(f: A => B): List[B] = {
//       val futures = xs.map(x => Future({Thread.sleep(10000);f(x)})(ec))
//       Await.result(futures.sequence, 200.seconds)
//     } 

//ec.shutdown 


