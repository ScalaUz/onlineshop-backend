package uz.scala.onlineshop.repos.dto

import eu.timepit.refined.types.string.NonEmptyString
import io.scalaland.chimney.dsl.TransformerOps
import tsec.passwordhashers.PasswordHash
import tsec.passwordhashers.jca.SCrypt
import uz.scala.onlineshop.EmailAddress
import uz.scala.onlineshop.domain.UserId

case class User(
    id: UserId,
    name: NonEmptyString,
    email: EmailAddress,
    password: PasswordHash[SCrypt],
    createdAt: java.time.ZonedDateTime,
    updatedAt: Option[java.time.ZonedDateTime] = None,
    deletedAt: Option[java.time.ZonedDateTime] = None,
  ) {
  def toDomain: uz.scala.onlineshop.domain.AuthedUser.User =
    this.transformInto[uz.scala.onlineshop.domain.AuthedUser.User]
}
