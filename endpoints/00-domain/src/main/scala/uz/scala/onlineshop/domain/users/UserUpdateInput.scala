package uz.scala.onlineshop.domain.users

import eu.timepit.refined.types.string.NonEmptyString

case class UserUpdateInput(
    name: NonEmptyString
  )
