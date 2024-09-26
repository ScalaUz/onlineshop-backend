package uz.scala.onlineshop.domain.categories

import eu.timepit.refined.types.string.NonEmptyString
import uz.scala.onlineshop.domain.CategoryId

case class Category(
    id: CategoryId,
    name: NonEmptyString,
  )
