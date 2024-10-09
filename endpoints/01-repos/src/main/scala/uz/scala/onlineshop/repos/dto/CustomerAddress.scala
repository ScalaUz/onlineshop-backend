package uz.scala.onlineshop.repos.dto

import java.time.ZonedDateTime

import eu.timepit.refined.types.string.NonEmptyString
import io.scalaland.chimney.dsl.TransformerOps
import uz.scala.onlineshop.domain
import uz.scala.onlineshop.domain._

case class CustomerAddress(
    customerId: CustomerId,
    countryId: CountryId,
    regionId: RegionId,
    cityId: CityId,
    street: NonEmptyString,
    postalCode: NonEmptyString,
    updatedAt: Option[ZonedDateTime] = None,
  ) {
  def toDomain: domain.customers.CustomerAddress =
    this.transformInto[domain.customers.CustomerAddress]
}
