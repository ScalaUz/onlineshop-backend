package uz.scala.onlineshop.repos.dto

import eu.timepit.refined.types.string.NonEmptyString
import io.scalaland.chimney.dsl.TransformerOps
import uz.scala.onlineshop.Phone
import uz.scala.onlineshop.domain.CustomerId

case class Customer(
    id: CustomerId,
    name: NonEmptyString,
    phone: Phone,
    createdAt: java.time.ZonedDateTime,
    updatedAt: Option[java.time.ZonedDateTime] = None,
    deletedAt: Option[java.time.ZonedDateTime] = None,
  ) {
  def toDomain: uz.scala.onlineshop.domain.AuthedUser.Customer =
    this.transformInto[uz.scala.onlineshop.domain.AuthedUser.Customer]

  def toView: uz.scala.onlineshop.domain.customers.Customer =
    this.transformInto[uz.scala.onlineshop.domain.customers.Customer]
}
