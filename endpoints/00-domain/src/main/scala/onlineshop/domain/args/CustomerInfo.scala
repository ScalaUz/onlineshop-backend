package onlineshop.domain.args

import eu.timepit.refined.types.string.NonEmptyString
import io.circe.generic.JsonCodec
import io.circe.refined._

import onlineshop.Phone

@JsonCodec
case class CustomerInfo(
    firstname: NonEmptyString,
    lastname: NonEmptyString,
    phone: Phone,
    password: NonEmptyString,
  )
