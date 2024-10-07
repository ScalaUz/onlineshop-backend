package uz.scala.onlineshop.domain.categories

import eu.timepit.refined.types.string.NonEmptyString
import uz.scala.onlineshop.domain.CategoryId

case class CategoryUpdateInput(
    id: CategoryId,
    nameUz: Option[NonEmptyString],
    nameRu: Option[NonEmptyString],
    nameEn: Option[NonEmptyString],
  )
