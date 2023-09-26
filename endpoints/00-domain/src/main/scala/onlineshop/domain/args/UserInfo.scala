package onlineshop.domain.args

import eu.timepit.refined.types.string.NonEmptyString

import onlineshop.Phone
import onlineshop.domain.enums.Role

case class UserInfo(
    firstname: NonEmptyString,
    lastname: NonEmptyString,
    phone: Phone,
    password: NonEmptyString,
    role: Role,
  )
