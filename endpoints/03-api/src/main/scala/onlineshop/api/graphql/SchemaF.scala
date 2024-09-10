package onlineshop.api.graphql

import scala.language.experimental.macros

import caliban.interop.cats.FromEffect
import caliban.schema.Schema

trait SchemaF[F[_], R, T[_[_]]] {
  implicit def schema(
      implicit
      fromEffect: FromEffect[F, R]
    ): Schema[R, T[F]]
}

object SchemaF {
  @inline def apply[F[_], R, T[_[_]]](f: FromEffect[F, R] => Schema[R, T[F]]): SchemaF[F, R, T] =
    f(_: FromEffect[F, R])
}
