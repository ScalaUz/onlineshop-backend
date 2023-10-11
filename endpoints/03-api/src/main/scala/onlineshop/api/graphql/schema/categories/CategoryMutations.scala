package onlineshop.api.graphql.schema.categories

import caliban.schema.Annotations.{GQLDescription, GQLName}
import onlineshop.algebras.Categories
import onlineshop.domain.CategoryId
import onlineshop.domain.args.CategoryInput

@GQLName("CategoryMutations")
case class CategoryMutations[F[_]](
    @GQLDescription("Create category")
    create: CategoryInput => F[CategoryId]
  )

object CategoryMutations {
  def make[F[_]](
      categoriesAlgebra: Categories[F]
    ): CategoryMutations[F] =
    CategoryMutations[F](
      create = input => categoriesAlgebra.create(input)
    )
}
