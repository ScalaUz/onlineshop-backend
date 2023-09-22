package onlineshop.domain

import java.time.ZonedDateTime

import eu.timepit.refined.types.string.NonEmptyString
import io.circe.generic.JsonCodec
import io.circe.refined._

import onlineshop.Phone
import onlineshop.domain.enums.Role

@JsonCodec
sealed trait AuthedUser {
  val id: PersonId
  val firstname: NonEmptyString
  val lastname: NonEmptyString
  val role: Role
  val phone: Phone
}

object AuthedUser {
  @JsonCodec
  case class User(
      id: PersonId,
      createdAt: ZonedDateTime,
      firstname: NonEmptyString,
      lastname: NonEmptyString,
      role: Role,
      phone: Phone,
    ) extends AuthedUser

  @JsonCodec
  case class Customer(
      id: PersonId,
      createdAt: ZonedDateTime,
      firstname: NonEmptyString,
      lastname: NonEmptyString,
      phone: Phone,
    ) extends AuthedUser {
    override val role: Role = Role.Customer
  }
}
