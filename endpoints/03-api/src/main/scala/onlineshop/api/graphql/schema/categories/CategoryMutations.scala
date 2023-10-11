package onlineshop.api.graphql.schema.categories

import caliban.schema.Annotations.GQLName

import onlineshop.algebras.Categories
import onlineshop.domain.CategoryId
import onlineshop.domain.args.CategoryInput

@GQLName("Categories")
case class CategoryMutations[F[_]](
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
