package onlineshop.api.graphql.schema.brands

import caliban.schema.Annotations.GQLDescription
import caliban.schema.Annotations.GQLName

import onlineshop.algebras.Brands
import onlineshop.domain.BrandId
import onlineshop.domain.args.BrandInput
@GQLName("BrandsMutations")
case class BrandsMutations[F[_]](
    @GQLDescription("Create brand")
    create: BrandInput => F[BrandId]
  )

object BrandsMutations {
  def make[F[_]](
      brandsAlgebra: Brands[F]
    ): BrandsMutations[F] =
    BrandsMutations[F](
      create = brandInput => brandsAlgebra.create(brandInput)
    )
}
