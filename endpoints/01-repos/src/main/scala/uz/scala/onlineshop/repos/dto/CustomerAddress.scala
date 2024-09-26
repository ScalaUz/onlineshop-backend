package uz.scala.onlineshop.repos.dto

import java.time.ZonedDateTime

import eu.timepit.refined.types.string.NonEmptyString
import uz.scala.onlineshop.domain._

case class CustomerAddress(
    customerId: CustomerId,
    countryId: CountryId,
    regionId: RegionId,
    cityId: CityId,
    street: NonEmptyString,
    postalCode: NonEmptyString,
    updatedAt: Option[ZonedDateTime] = None,
  )
