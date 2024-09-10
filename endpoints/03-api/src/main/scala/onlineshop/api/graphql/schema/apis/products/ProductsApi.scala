package onlineshop.api.graphql.schema.apis.products

import cats.Monad
import cats.implicits.catsSyntaxOptionId
import cats.implicits.none

import onlineshop.algebras.ProductsAlgebra
import onlineshop.api.graphql.schema.GraphqlApi

class ProductsApi[F[_]](queries: Queries[F]) extends GraphqlApi(queries.some, none[Mutations[F]])

object ProductsApi {
  def make[F[_]: Monad](
      productsAlgebra: ProductsAlgebra[F]
    ): GraphqlApi.Any[F, Any] =
    new ProductsApi[F](
      queries = Queries[F](
        products = productsAlgebra.fetchAll
      )
    )
}
