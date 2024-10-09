package uz.scala.onlineshop.domain

import eu.timepit.refined.types.string.NonEmptyString

case class Country(
    id: CountryId,
    name: NonEmptyString,
  )
