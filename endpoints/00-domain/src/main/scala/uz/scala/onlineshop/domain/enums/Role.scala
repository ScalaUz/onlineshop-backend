package uz.scala.onlineshop.domain.enums

import enumeratum.EnumEntry.Snakecase
import enumeratum._

sealed trait Role extends Snakecase
object Role extends Enum[Role] with CirceEnum[Role] {
  case object Admin extends Role
  case object Moderator extends Role
  case object Customer extends Role
  override def values: IndexedSeq[Role] = findValues
}
