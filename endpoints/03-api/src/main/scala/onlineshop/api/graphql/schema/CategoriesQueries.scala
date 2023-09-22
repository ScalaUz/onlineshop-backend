package onlineshop.api.graphql.schema

import caliban.schema.Annotations.GQLName
import onlineshop.algebras.Categories
import onlineshop.domain.Category
@GQLName("Categories")
case class CategoriesQueries[F[_]](
    get: F[List[Category]]
  )

object CategoriesQueries extends GraphQLTypes {
  def make[F[_]](
      categoriesAlgebra: Categories[F]
    ): CategoriesQueries[F] =
    CategoriesQueries[F](
      get = categoriesAlgebra.fetchAll
    )
}
