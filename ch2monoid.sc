// Examples of monoid with simple types and operations ////////////////////////

// type classes
trait MyTypeClass[A] {
  def doSomethingOnA(a: A): String
  def plus(a: A, b: A): A
}

object MyTypeClass {
  implicit val myTypeClassInt: MyTypeClass[Int] =
    new MyTypeClass[Int] {
      def doSomethingOnA(a: Int): String = a.toString
      def plus(a: Int, b: Int): Int = a + b
    }
  implicit val myTypeClassString: MyTypeClass[String] =
    new MyTypeClass[String] {
      def doSomethingOnA(a: String): String = a.toString
      def plus(a: String, b: String): String = a ++ b
    }

  def doSomethingOnA[A](
    a: A
  )(implicit mtc: MyTypeClass[A]): String =
    mtc.doSomethingOnA(a)

}

implicit class MyTypeClassOps[A](a: A) {
  def doSomethingOnA(implicit mtc: MyTypeClass[A]): String =
    mtc.doSomethingOnA(a)
}

MyTypeClass.myTypeClassInt.doSomethingOnA(2)

MyTypeClass.doSomethingOnA(2)

MyTypeClass.doSomethingOnA("asdfasdfasfd")

"asfasd".doSomethingOnA
1 doSomethingOnA

1.+(2)

2 + 0

// def +(x: Int, y: Int): Int
def plus(x: Int, y: Int): Int = x + y

//  +(x: Int, y: Int): Int

1 * 2 * 1
1 * (2 * 1)
(1 * 2) * 1

"One" ++ "two"
"" ++ "two"
"Hello" ++ ""

"One" ++ "two" ++ "three"
("One" ++ "two") ++ "three"
"One" ++ ("two" ++ "three")

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

"sadasdf" ++ ""

1 + 0 == 1
0 + 1 == 1

// identity law examples
"sadasdf" ++ "" == "" + "sadasdf"
"sadasdf" ++ "" == "sadasdf"
"" + "sadasdf" == "sadasdf"
// identity law where `combine == +` and empty = 0
1 + 0 == 1
0 + 1 == 1

// identity law where `combine == *` and empty = 1
5 * 1 == 5
1 * 5 == 5

// identity law where `combine == *` and empty = 0
5 * 0 == 5
0 * 5 == 5

// associativity law combine is `+`

(5 + 6) + 7 == 5 + (6 + 7)

// associativity law combine is `-`

(5 - 6) - 7 == 5 - (6 - 7)

// Ex 2.3 Define monoid for Boolean

trait Semigroup[A] {
  def combine(x: A, y: A): A
}

trait Monoid[A] extends Semigroup[A] {
  def empty: A
}

object Monoid {
  // implicit val booleanAndMonoid: Monoid[Boolean] = new Monoid[Boolean] {
  //   def combine(x: Boolean, y: Boolean): Boolean = x && y
  //   def empty: Boolean = true
  // }

  implicit val booleanOrMonoid: Monoid[Boolean] = new Monoid[Boolean] {
    def combine(x: Boolean, y: Boolean): Boolean = x || y
    def empty: Boolean = false
  }
  def apply[A](implicit monoid: Monoid[A]) = monoid
}

Monoid[Boolean].combine(true, false)
Monoid[Boolean].empty

Monoid[Boolean].combine(true, Monoid[Boolean].empty)
Monoid[Boolean].combine(false, Monoid[Boolean].empty)

// Ex 2.4 Set Monoid

trait Semigroup[A] {
  def combine(x: A, y: A): A
}

trait Monoid[A] extends Semigroup[A] {
  def empty: A
}

object Monoid {

  implicit def setMonoid[A]: Monoid[Set[A]] = new Monoid[Set[A]] {
    def combine(x: Set[A], y: Set[A]): Set[A] = (x diff y) union (y diff x)
    def empty: Set[A] = Set()
  }

  def apply[A](implicit monoid: Monoid[A]) = monoid
}

Monoid[Set[Int]].combine(Set(1, 2, 3), Set(2, 5, 6))

Monoid[Set[String]].combine(Set("asdfa", "asdfas", "asdfasdfasdfasdf"), Monoid[Set[String]].empty)

interp.configureCompiler(_.settings.fatalWarnings.value = true)
interp.configureCompiler(_.settings.YpartialUnification.value = true)

import $ivy.`org.typelevel::cats-core:1.0.0`
import cats.Show

import cats.Monoid
import cats.Semigroup
import cats.instances.int._
import cats.instances.string._
import cats.instances.option._

Monoid[Int].combine(1, 2)
Monoid[String].combine("asdfasdf", "second string")

Monoid[Option[Int]].combine(Option(1), Option(2))

Monoid[String]

Monoid[Int]
import cats.syntax.semigroup._ // for |+|

("asdas" |+| "asasdf") |+| "asdfasdasdf"

"asdas" |+| ("asasdf" |+| "asdfasdasdf")

val intResult = 1 |+| 2 |+| Monoid[Int].empty

// Ex: 2.5.4

def add(items: List[Int]): Int = items.fold[Int](0)((a, b) => a + b)

add(List(1, 2, 3))

import cats.Monoid
import cats.Semigroup
import cats.instances.int._
import cats.instances.string._
import cats.instances.option._

def add[A](items: List[A])(implicit m: Monoid[A]): A = items.fold[A](m.empty)((a, b) => m.combine(a, b))

add(List(Option(1), Option(2), Option(3)))
add(List(1, 2, 3))

add(List("1", "2", "3"))

case class Order(totalCost: Double, quantity: Double)

implicit val orderMonoid: Monoid[Order] = new Monoid[Order] {
  def combine(x: Order, y: Order): Order = Order(x.totalCost + y.totalCost, x.quantity + y.quantity)
  def empty: Order = Order(0, 0)
}

add(List(Order(1, 2), Order(3, 4), Order(5, 6)))

import cats.instances.map._ // for Monoid

val map1 = Map("a" -> 1, "b" -> 2)
val map2 = Map("b" -> 3, "d" -> 4)
map1 |+| map2

import cats.instances.tuple._

// for Monoid
val tuple1 = ("hello", 123)
val tuple2 = ("world", 321)
tuple1 |+| tuple2

def addAll[A](values: List[A])(implicit monoid: Monoid[A]): A =
  values.foldRight(monoid.empty)(_ |+| _)

addAll(List(1, 2, 3))

addAll(List(None, Some(1), Some(2)))
