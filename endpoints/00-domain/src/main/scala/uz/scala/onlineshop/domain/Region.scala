package uz.scala.onlineshop.domain

import eu.timepit.refined.types.string.NonEmptyString

case class Region(
    id: RegionId,
    countryId: CountryId,
    name: NonEmptyString,
  )
