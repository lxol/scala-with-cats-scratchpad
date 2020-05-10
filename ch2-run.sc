// ch2 Monoid and Semigroup

// 1.
trait MyTypeClass[A] {
  def doSomething(a: A): String
}

// 2.

object MyTypeClass {
  implicit val stringMyTypeClass: MyTypeClass[String] = new MyTypeClass[String] {
    def doSomething(a: String): String = a
  }
  implicit val intMyTypeClass: MyTypeClass[Int] = new MyTypeClass[Int] {
    def doSomething(a: Int): String = a.toString
  }
  def doSomething[A](a: A)(implicit mtc: MyTypeClass[A]): String = mtc.doSomething(a)
}

// 3. Interface

stringMyTypeClass.doSomething("asdfasdf")

MyTypeClass.doSomething("asdfasdfasfasfasf")

MyTypeClass.doSomething(1)

// A + A = A
// def combine(x: A, y: A): A

2 + 1

2 + (3 + 5)

(2 + 3) + 5

2 + 0

0 + 2

2 * (3 * 5)

(2 * 3) * 5

2 * 1

1 * 2

"asdfasdf" ++ "asdfasdf"

trait Monoid[A] {
  def combine(x: A, y: A): A
  def empty: A
}

object MonoidLaws {

  def associativeLaw[A](x: A, y: A, z: A)(implicit m: Monoid[A]): Boolean =
    m.combine(x, m.combine(y, z)) ==
      m.combine(m.combine(x, y), z)

  def identityLaw[A](x: A)(implicit m: Monoid[A]): Boolean =
    (m.combine(x, m.empty) == x) &&
      (m.combine(m.empty, x) == x)
}
// (x + (y + z)) == ((x + y) + z)

(1 - 2) - 3

1 - (2 - 3)

trait Semigroup[A] {
  def combine(x: A, y: A): A
}

trait Monoid[A] extends Semigroup[A] {
  def empty: A
}

object Monoid {
  // implicit val intMonoid: Monoid[Int] = new Monoid[Int] {
  //   def combine(x: Int, y: Int): Int  = x + y
  //   def empty: Int = 0
  // }
  implicit val intMonoid: Monoid[Int] = new Monoid[Int] {
    def combine(x: Int, y: Int): Int  = x - y
    def empty: Int = 0
  }

  // implicit val boolAndMonoid: Monoid[Boolean] = new Monoid[Boolean] {
  //   def combine(x: Boolean, y: Boolean): Boolean  = x && y
  //   def empty: Boolean = true
  // }

  implicit val boolOrMonoid: Monoid[Boolean] = new Monoid[Boolean] {
    def combine(x: Boolean, y: Boolean): Boolean  = x || y
    def empty: Boolean = false
  }
}

Monoid.intMonoid.combine(2,Monoid.intMonoid.empty)

interp.configureCompiler(_.settings.fatalWarnings.value = true)
interp.configureCompiler(_.settings.YpartialUnification.value = true)

import $ivy.`org.typelevel::cats-core:1.0.0`


import cats.Monoid
import cats.Semigroup

import cats.instances.string._ // for Monoid

Monoid[String]

Semigroup[String].combine("Hi ", "there")

Semigroup[Int].combine(1, 2)


import cats.instances.int._ // for Monoid

import cats.instances.option._ // for Monoid

Semigroup[Option[Int]].combine(Option(1), Option(2))

import cats.syntax.semigroup._ // for |+|

1 |+| 2 |+| 3

"asdfadsf" |+| "  asdfasdf" |+| " asdfas"

def add(items: List[Int]): Int = items.fold[Int](0)((x,y) => x + y) 

add(List(1,3,10))

def add[A](items: List[A])(implicit m: Monoid[A]): A = items.fold[A](m.empty)(_ |+| _ ) 

add(List("asdfasdf"," second", " third"))

add(List(Option("asdfasdf"),Option(" second"), Option(" third")))

add(List(Option("asdfasdf"),Option(" second"), None))

case class Order(totalCost: Double, quantity: Double)

implicit val orderMonoid: Monoid[Order] = new Monoid[Order] {
    def combine(x: Order, y: Order): Order  = 
    def empty: Order = false

}
