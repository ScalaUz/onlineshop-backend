package uz.scala.onlineshop.api.graphql.schema.apis.brands

import caliban.schema.Annotations.GQLDescription
import uz.scala.onlineshop.domain.BrandId
import uz.scala.onlineshop.domain.brands.BrandInput
import uz.scala.onlineshop.domain.brands.BrandUpdateInput

case class Mutations[F[_]](
    @GQLDescription("Create brand")
    createBrand: BrandInput => F[BrandId],
    @GQLDescription("Update brand")
    updateBrand: BrandUpdateInput => F[Unit],
    @GQLDescription("Delete brand")
    deleteBrand: BrandId => F[Unit],
  )
