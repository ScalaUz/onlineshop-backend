package uz.scala.onlineshop.domain.customers

import eu.timepit.refined.types.string.NonEmptyString
import uz.scala.onlineshop.Phone
import uz.scala.onlineshop.domain.CustomerId

case class Customer(
    id: CustomerId,
    name: NonEmptyString,
    phone: Phone,
    createdAt: java.time.ZonedDateTime,
  )
