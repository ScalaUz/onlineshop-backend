package uz.scala.onlineshop.api.graphql.schema.apis.products

import caliban.schema.Annotations.GQLDescription
import uz.scala.onlineshop.domain.ProductId
import uz.scala.onlineshop.domain.products.Product

case class Queries[F[_]](
    @GQLDescription("Fetch all products")
    products: F[List[Product]],
    @GQLDescription("Fetch product by id")
    productById: ProductId => F[Option[Product]],
  )
