package uz.scala.onlineshop.repos.sql

import skunk.Codec
import uz.scala.onlineshop.effects.IsUUID

abstract class Sql[T: IsUUID] {
  val id: Codec[T] = identification[T]
}
