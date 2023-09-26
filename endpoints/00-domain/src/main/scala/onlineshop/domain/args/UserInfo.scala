package onlineshop.domain.args

import eu.timepit.refined.types.string.NonEmptyString
import io.circe.generic.JsonCodec

import onlineshop.Phone
import onlineshop.domain.enums.Role
import io.circe.refined._
@JsonCodec
case class UserInfo(
    firstname: NonEmptyString,
    lastname: NonEmptyString,
    phone: Phone,
    password: NonEmptyString,
    role: Role,
  )
