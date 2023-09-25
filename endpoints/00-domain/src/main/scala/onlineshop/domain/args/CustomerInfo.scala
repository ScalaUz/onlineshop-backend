package onlineshop.domain.args

import eu.timepit.refined.types.string.NonEmptyString

import onlineshop.Phone

case class CustomerInfo(
    firstname: NonEmptyString,
    lastname: NonEmptyString,
    phone: Phone,
    password: NonEmptyString,
  )