package onlineshop.api.graphql.schema

import onlineshop.algebras.Products
import onlineshop.domain.Product
case class ProductsQueries[F[_]](
    fetchAll: F[List[Product]]
  )

object ProductsQueries extends GraphQLTypes {
  def make[F[_]](
      productsAlgebra: Products[F]
    ): ProductsQueries[F] =
    ProductsQueries[F](
      fetchAll = productsAlgebra.fetchAll
    )
}
