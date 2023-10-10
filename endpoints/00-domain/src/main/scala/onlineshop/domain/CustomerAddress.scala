package onlineshop.domain

import eu.timepit.refined.types.string.NonEmptyString
import io.circe.generic.JsonCodec
import io.circe.refined._

@JsonCodec
case class CustomerAddress(
    customerId: PersonId,
    countryId: CountryId,
    regionId: RegionId,
    cityId: CityId,
    street: NonEmptyString,
    postalCode: NonEmptyString,
  )
