package uz.scala.onlineshop.domain.users

import eu.timepit.refined.types.string.NonEmptyString
import uz.scala.onlineshop.EmailAddress

case class UserInput(
    name: NonEmptyString,
    email: EmailAddress,
  )
