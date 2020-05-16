interp.configureCompiler(_.settings.fatalWarnings.value = true)
interp.configureCompiler(_.settings.YpartialUnification.value = true)

import $ivy.`org.typelevel::cats-core:1.0.0`

List(1, 2, 3).map(n => n + 1)

trait Functor[F[_]] {
  def map[A, B](fa: F[A])(f: A => B): F[B]
  def lift
}

List(1, 2, 3).map(a => a)

trait Printable[A] { => self
  def format(value: A): String
  def contramap[B](func: B => A): Printable[B] =
    new Printable[B] {
      def format(value: B): String =
         self.format(func(value)) 
    }
}
