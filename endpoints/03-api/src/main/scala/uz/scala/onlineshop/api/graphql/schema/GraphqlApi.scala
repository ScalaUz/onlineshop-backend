package uz.scala.onlineshop.api.graphql.schema

import caliban._
import caliban.interop.cats.FromEffect

abstract class GraphqlApi[F[_], R] {
  def graphql(
      implicit
      effect: FromEffect[F, R]
    ): GraphQL[R]
}
