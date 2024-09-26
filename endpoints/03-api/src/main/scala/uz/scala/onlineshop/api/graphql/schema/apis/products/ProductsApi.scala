package uz.scala.onlineshop.api.graphql.schema.apis.products

import caliban.GraphQL
import caliban.RootResolver
import caliban.graphQL
import caliban.interop.cats.FromEffect
import cats.Monad
import cats.effect.std.Dispatcher
import uz.scala.onlineshop.Language
import uz.scala.onlineshop.algebras.ProductsAlgebra
import uz.scala.onlineshop.api.graphql.GraphQLContext
import uz.scala.onlineshop.api.graphql.schema.GraphqlApi

class ProductsApi[F[_]: Dispatcher](queries: Queries[F]) extends GraphqlApi[F, GraphQLContext] {
  override def graphql(
      implicit
      effect: FromEffect[F, GraphQLContext]
    ): GraphQL[GraphQLContext] =
    graphQL(RootResolver(queries))
}

object ProductsApi {
  def make[F[_]: Monad: Dispatcher](
      productsAlgebra: ProductsAlgebra[F]
    )(implicit
      lang: Language
    ): GraphqlApi[F, GraphQLContext] =
    new ProductsApi[F](
      queries = Queries[F](
        products = productsAlgebra.fetchAll
      )
    )
}
