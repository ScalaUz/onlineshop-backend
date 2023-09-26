package onlineshop.api.graphql.schema.products

import caliban.schema.Annotations.GQLDescription
import caliban.schema.Annotations.GQLName

import onlineshop.algebras.Products
import onlineshop.api.graphql.schema.Utils.Access
import onlineshop.domain.Product
import onlineshop.domain.args.ProductArgs
import onlineshop.domain.enums.Role

@GQLName("Products")
case class ProductsQueries[F[_]](
    @Access(Role.TechAdmin)
    @GQLDescription("Fetch all products")
    get: ProductArgs => F[List[Product]]
  )

object ProductsQueries {
  def make[F[_]](
      productsAlgebra: Products[F]
    ): ProductsQueries[F] =
    ProductsQueries[F](
      get = args => productsAlgebra.fetchAll
    )
}
