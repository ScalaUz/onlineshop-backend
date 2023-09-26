package onlineshop.api.graphql.schema.categories

import caliban.schema.Annotations.GQLDescription
import caliban.schema.Annotations.GQLName

import onlineshop.algebras.Categories
import onlineshop.domain.Category
@GQLName("Categories")
case class CategoriesQueries[F[_]](
    @GQLDescription("Fetch all category")
    get: F[List[Category]]
  )

object CategoriesQueries {
  def make[F[_]](
      categoriesAlgebra: Categories[F]
    ): CategoriesQueries[F] =
    CategoriesQueries[F](
      get = categoriesAlgebra.fetchAll
    )
}
