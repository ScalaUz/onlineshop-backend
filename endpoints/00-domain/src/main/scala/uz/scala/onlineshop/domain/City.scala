package uz.scala.onlineshop.domain

import eu.timepit.refined.types.string.NonEmptyString

case class City(
    id: CityId,
    regionId: RegionId,
    nameUz: NonEmptyString,
    nameRu: NonEmptyString,
    nameEn: NonEmptyString,
  )
