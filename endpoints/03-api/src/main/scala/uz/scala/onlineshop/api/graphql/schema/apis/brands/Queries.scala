package uz.scala.onlineshop.api.graphql.schema.apis.brands

import caliban.schema.Annotations.GQLDescription
import uz.scala.onlineshop.domain.BrandId
import uz.scala.onlineshop.domain.brands.Brand

case class Queries[F[_]](
    @GQLDescription("Fetch all brands")
    brands: F[List[Brand]],
    @GQLDescription("Fetch brand by id")
    brandById: BrandId => F[Option[Brand]],
  )
