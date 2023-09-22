package onlineshop.api.graphql.schema

import caliban.schema.Annotations.GQLName

import onlineshop.algebras.Products
import onlineshop.api.graphql.args.ProductArgs
import onlineshop.domain.Product

@GQLName("Products")
case class ProductsQueries[F[_]](
    get: ProductArgs => F[List[Product]]
  )

object ProductsQueries extends GraphQLTypes {
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
