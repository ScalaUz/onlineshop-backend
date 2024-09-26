package uz.scala.onlineshop.domain.auth

import eu.timepit.refined.types.string.NonEmptyString
import uz.scala.onlineshop.EmailAddress

case class UserCredentials(email: EmailAddress, password: NonEmptyString)
