package uz.scala.onlineshop.domain.categories

import eu.timepit.refined.types.string.NonEmptyString

case class CategoryInput(
    nameUz: NonEmptyString,
    nameRu: NonEmptyString,
    nameEn: NonEmptyString,
  )
