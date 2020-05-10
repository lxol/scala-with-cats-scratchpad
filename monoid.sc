// extract common principles

(1 + 2) + 3

1 + (2 + 3)

(1 * 2) * 3

1 * (2 * 3)

"One" ++ "two"

"One" ++ "two" ++ "tree"

"One" ++ ("two" ++ "tree")
("One" ++ "two") ++ "tree"

0 + 25

1 * 25

"" ++ "something" == "something"

// 2.1 Defining of a Monoid

// trait Monoid[A] {
//   def combine(x: A, y: A): A
//   def empty: A
// }

trait Semigroup[A] {
  def combine(x: A, y: A): A
}
trait Monoid[A] extends Semigroup[A] {
  def combine(x: A, y: A): A
  def empty: A
}
object Monoid {
  def apply[A](implicit monoid: Monoid[A]) = monoid
}

object MonoidLaws {

  def associativeLaw[A](x: A, y: A, z: A)(implicit m: Monoid[A]): Boolean = {
    m.combine(x, m.combine(y, z)) ==
      m.combine(m.combine(x, y), z)
  }
  def identityLaw[A](x: A)(implicit m: Monoid[A]): Boolean = {
    (m.combine(x, m.empty) == x) &&
    (m.combine(m.empty, x) == x)
  }
}

object BooleanAndMonoid {
  implicit val monoidFromBoolean: Monoid[Boolean] = new Monoid[Boolean] {
    def combine(x: Boolean, y: Boolean): Boolean = x && y
    def empty: Boolean = true
  }
}

object BooleanOrMonoid {
  implicit val monoidOrFromBoolean: Monoid[Boolean] = new Monoid[Boolean] {
    def combine(x: Boolean, y: Boolean): Boolean = x || y
    def empty: Boolean = false
  }
}

object SetMonoid {
  implicit def monoidOfSet[A]: Monoid[Set[A]] = new Monoid[Set[A]] {
    override def combine(x: Set[A], y: Set[A]): Set[A] =  x ++ y
    override def empty: Set[A] = Set()
    def foo = ""
  }
}


object SetOnlyMonoid {
  implicit def monoidOfSet[A]: Monoid[Set[A]] = new Monoid[Set[A]] {
    override def combine(x: Set[A], y: Set[A]): Set[A] =  x ++ y
    override def empty: Set[A] = Set()
    def foo = ""
  }
}
