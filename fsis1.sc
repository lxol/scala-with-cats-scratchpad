interp.configureCompiler(_.settings.fatalWarnings.value = true)
interp.configureCompiler(_.settings.YpartialUnification.value = true)

import $ivy.`com.lihaoyi::fansi:0.2.5`
import $ivy.`com.github.mpilquist::simulacrum:0.19.0`
import $ivy.`org.spire-math::kind-projector:0.9.10`

/**

  * In the previous chapter we discussed type classes.  as a way to
  * introduce abstraction.  In other words we can program using
  * abstraction assuming that any implementation of the abstration
  * will be lawful.

  *  Chapter 3 of scala-with-cats - intruduces such an abstraction
  *  (Functor).
  * In the previous chapter  we discussed type classes.
  * as a way to introduce abstraction.
  * In other words we can program using abstraction assuming that any
  * implementation of the abstration will be lawful.

  * Also we discussed Monoids and Semigroups
  * Show monoid copy-paste and introduce Functor


  */
// abstracting over any "real" type
trait Monoid[A] {
  def compose(a: A, b: A): A
  def empty: A
}

// Abstracting over type constructor
// like list, Option, List
// show in the scala terminal kind of List, Either, Option
// trait Functor[F[_]] {
//   // def compose(a: A, b: A): A
//   // def empty: A
// }
// Show different containers //////////////////////////////////////////////////
// Future, List, Option, Either are not real types
// They are type constructors that create types

// scala> import scala.concurrent.Future
// import scala.concurrent.Future

// scala> :k Future
// scala.concurrent.Future's kind is F[+A]

// scala> :k List
// List's kind is F[+A]

// scala> :k Option
// Option's kind is F[+A]

// scala> :k Either
// Either's kind is F[+A1,+A2]

// scala> 


// Functor definition in the scala-with-cats book
// Functor is anything with a map method

trait Functor[F[_]] {
  def map[A, B](fa: F[A])(f: A => B): F[B]
}

trait FunctorLaws {
//  def identity[F[_], A](fa: F[A])(implicit F: Functor[F]) =

 // def composition[F[_], A]
}


object Functor {

}
 
