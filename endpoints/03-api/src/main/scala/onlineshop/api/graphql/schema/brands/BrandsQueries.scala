package onlineshop.api.graphql.schema.brands

import caliban.schema.Annotations.GQLDescription
import caliban.schema.Annotations.GQLName

import onlineshop.algebras.Brands
import onlineshop.domain.Brand
@GQLName("BrandsQueries")
case class BrandsQueries[F[_]](
    @GQLDescription("Fetch all brands")
    get: F[List[Brand]]
  )

object BrandsQueries {
  def make[F[_]](
      brandsAlgebra: Brands[F]
    ): BrandsQueries[F] =
    BrandsQueries[F](
      get = brandsAlgebra.fetchAll
    )
}
