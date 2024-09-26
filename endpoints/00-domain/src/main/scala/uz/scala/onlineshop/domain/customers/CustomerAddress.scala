package uz.scala.onlineshop.domain.customers

import eu.timepit.refined.types.string.NonEmptyString
import uz.scala.onlineshop.domain.CityId
import uz.scala.onlineshop.domain.CountryId
import uz.scala.onlineshop.domain.CustomerId
import uz.scala.onlineshop.domain.RegionId

case class CustomerAddress(
    customerId: CustomerId,
    countryId: CountryId,
    regionId: RegionId,
    cityId: CityId,
    street: NonEmptyString,
    postalCode: NonEmptyString,
  )
