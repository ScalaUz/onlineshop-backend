package uz.scala.onlineshop.api.graphql.schema.apis.products

import caliban.schema.Annotations.GQLDescription
import uz.scala.onlineshop.domain.ProductId
import uz.scala.onlineshop.domain.products.ProductInput
import uz.scala.onlineshop.domain.products.ProductUpdateInput

case class Mutations[F[_]](
    @GQLDescription("Create product")
    createProduct: ProductInput => F[Unit],
    @GQLDescription("Update product")
    updateProduct: ProductUpdateInput => F[Unit],
    @GQLDescription("Delete product")
    deleteProduct: ProductId => F[Unit],
  )
