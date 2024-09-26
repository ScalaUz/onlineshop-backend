package uz.scala.onlineshop.domain

import eu.timepit.refined.types.string.NonEmptyString
import io.circe.generic.JsonCodec
import io.circe.refined._
import uz.scala.onlineshop.{EmailAddress, Phone}
import uz.scala.onlineshop.domain.enums.Role

import java.util.UUID

@JsonCodec
sealed trait AuthedUser {
  val _id: UUID
  val name: NonEmptyString
  val role: Role
  val login: String
}

object AuthedUser {
    @JsonCodec
  case class User(
      id: UserId,
      name: NonEmptyString,
      email: EmailAddress,
    ) extends AuthedUser {
    override val _id: UUID = id.value
    override val login: String = email.value
    override val role: Role = Role.Admin
  }

  @JsonCodec
  case class Customer(
      id: CustomerId,
      name: NonEmptyString,
      phone: Phone,
    ) extends AuthedUser {
    override val _id: UUID = id.value
    override val login: String = phone.value
    override val role: Role = Role.Customer
  }
}
