package uz.scala.onlineshop.domain.auth

import eu.timepit.refined.types.string.NonEmptyString
import uz.scala.onlineshop.Phone

case class CustomerCredentials(phone: Phone, password: NonEmptyString)
