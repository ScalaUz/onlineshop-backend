package uz.scala.mailer.data

import eu.timepit.refined.types.string.NonEmptyString
import uz.scala.mailer.data.types.Password

case class Credentials(user: NonEmptyString, password: Password)
