package onlineshop.repos.sql

import onlineshop.effects.IsUUID
import skunk.Codec

abstract class Sql[T: IsUUID] {
  val id: Codec[T] = identification[T]
}
