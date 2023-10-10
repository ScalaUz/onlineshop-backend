package onlineshop.api.graphql.schema.products

import caliban.schema.Annotations.GQLDescription
import caliban.schema.Annotations.GQLName

import onlineshop.algebras.Products
import onlineshop.domain.Product
import onlineshop.domain.args.ProductArgs

@GQLName("Products")
case class ProductsQueries[F[_]](
    @GQLDescription("Fetch all products")
    get: ProductArgs => F[List[Product]]
  )

object ProductsQueries {
  def make[F[_]](
      productsAlgebra: Products[F]
    ): ProductsQueries[F] =
    ProductsQueries[F](
      get = args => {
        println(args)
        productsAlgebra.fetchAll
      }
    )
}
