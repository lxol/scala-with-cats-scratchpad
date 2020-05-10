///////////////////////////////////////////////////////////////////////////////
//                                  Chapter 2.
//                              Monoids and Semigroup
///////////////////////////////////////////////////////////////////////////////

interp.configureCompiler(_.settings.fatalWarnings.value = true)
interp.configureCompiler(_.settings.YpartialUnification.value = true)

import $ivy.`org.typelevel::cats-core:1.0.0`
import $ivy.`com.lihaoyi::fansi:0.2.5`
import $ivy.`com.github.mpilquist::simulacrum:0.19.0`
import $ivy.`org.spire-math::kind-projector:0.9.10`

import cats.Monoid
import cats.instances.string._ // for Monoid
import cats.syntax.semigroup._ // for |+|

"Scala" |+| " with " |+| "Cats"
import cats.instances.int._
import cats.instances.option._
import cats.instances.map._ // for Monoid

val map1 = Map("a" -> 1, "b" -> 2)
val map2 = Map("b" -> 3, "d" -> 4)
map1 |+| map2
import cats.instances.tuple._
val tuple1 = ("hello", 123)
val tuple2 = ("world", 321)
tuple1 |+| tuple2
// res6: (String, Int) = (hellow
def addAll[A](values: List[A])(implicit monoid: Monoid[A]): A =
  values.foldRight(monoid.empty)(_ |+| _)

trait Functor[F[_]] {
  def map[A, B](fa: F[A])(f: A => B): F[B]
}

trait FunctorLaws {
  // def identity[F[_], A](fa: F[A]) = Functor[F].map(fa)(a => a)
}
