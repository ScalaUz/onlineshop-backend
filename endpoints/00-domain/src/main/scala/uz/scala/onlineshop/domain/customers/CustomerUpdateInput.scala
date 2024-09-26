package uz.scala.onlineshop.domain.customers

import eu.timepit.refined.types.string.NonEmptyString

case class CustomerUpdateInput(
    name: NonEmptyString
  )
