package uz.scala.onlineshop.domain.users

import eu.timepit.refined.types.string.NonEmptyString
import uz.scala.onlineshop.domain.UserId

case class UserUpdateInput(
    id: UserId,
    name: NonEmptyString,
  )
