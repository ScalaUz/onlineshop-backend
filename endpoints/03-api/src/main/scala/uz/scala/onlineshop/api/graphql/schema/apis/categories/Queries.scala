package uz.scala.onlineshop.api.graphql.schema.apis.categories

import caliban.schema.Annotations.GQLDescription
import uz.scala.onlineshop.domain.CategoryId
import uz.scala.onlineshop.domain.categories.Category

case class Queries[F[_]](
    @GQLDescription("Fetch all categories")
    categories: F[List[Category]],
    @GQLDescription("Find category by id")
    categoryById: CategoryId => F[Option[Category]],
  )
