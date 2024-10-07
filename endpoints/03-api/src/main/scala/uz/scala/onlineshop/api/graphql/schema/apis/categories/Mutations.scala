package uz.scala.onlineshop.api.graphql.schema.apis.categories

import caliban.schema.Annotations.GQLDescription
import uz.scala.onlineshop.domain.CategoryId
import uz.scala.onlineshop.domain.categories.CategoryInput
import uz.scala.onlineshop.domain.categories.CategoryUpdateInput

case class Mutations[F[_]](
    @GQLDescription("Create category")
    createCategory: CategoryInput => F[CategoryId],
    @GQLDescription("Update category")
    updateCategory: CategoryUpdateInput => F[Unit],
    @GQLDescription("Delete category")
    deleteCategory: CategoryId => F[Unit],
  )
