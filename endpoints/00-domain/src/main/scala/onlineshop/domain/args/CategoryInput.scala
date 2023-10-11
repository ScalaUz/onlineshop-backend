package onlineshop.domain.args

import eu.timepit.refined.types.string.NonEmptyString

case class CategoryInput(
    nameUz: NonEmptyString,
    nameRu: NonEmptyString,
    nameEn: NonEmptyString,
  )
