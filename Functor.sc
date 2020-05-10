interp.configureCompiler(_.settings.fatalWarnings.value = true)
interp.configureCompiler(_.settings.YpartialUnification.value = true)

import $ivy.`org.typelevel::cats-core:1.0.0`
import $ivy.`com.lihaoyi::fansi:0.2.5`
import $ivy.`com.github.mpilquist::simulacrum:0.19.0`
import $ivy.`org.spire-math::kind-projector:0.9.10`

trait Functor[F[_]] {
  def map[A, B](fa: F[A])(f: A => B): F[B]
}

trait FunctorLaws {
  def identity[F[_], A](fa: F[A])(implicit  f: Functor[F]) = f.map(fa)(a => a)  === fa
}
