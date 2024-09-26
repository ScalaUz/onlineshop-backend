package uz.scala.onlineshop.domain.customers

import eu.timepit.refined.types.string.NonEmptyString
import uz.scala.onlineshop.Phone

case class CustomerInput(
    name: NonEmptyString,
    phone: Phone,
    password: NonEmptyString,
  )
