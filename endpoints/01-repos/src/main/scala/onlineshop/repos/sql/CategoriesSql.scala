package onlineshop.repos.sql

import skunk._
import skunk.codec.all.varchar
import skunk.implicits._

import onlineshop.domain.Category

private[repos] object CategoriesSql {
  private val codec: Codec[Category] = varchar.to[Category]
  val insert: Command[Category] =
    sql"""INSERT INTO categories VALUES ($codec)""".command

  val fetch: Query[Void, Category] =
    sql"""SELECT * FROM categories""".query(codec)
}
