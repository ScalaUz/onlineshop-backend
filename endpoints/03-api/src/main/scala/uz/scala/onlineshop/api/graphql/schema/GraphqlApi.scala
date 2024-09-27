package uz.scala.onlineshop.api.graphql.schema

import caliban._

abstract class GraphqlApi[F[_], R] {
  def graphql: GraphQL[R]
}
