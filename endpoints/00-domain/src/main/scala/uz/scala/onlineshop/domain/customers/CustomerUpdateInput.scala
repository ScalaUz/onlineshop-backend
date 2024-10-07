package uz.scala.onlineshop.domain.customers

import eu.timepit.refined.types.string.NonEmptyString
import uz.scala.onlineshop.domain.CustomerId

case class CustomerUpdateInput(
    id: CustomerId,
    name: NonEmptyString,
  )
