package onlineshop.repos.sql

import skunk._
import skunk.implicits._

import onlineshop.domain.Category
import onlineshop.domain.CategoryId

private[repos] object CategoriesSql extends Sql[CategoryId] {
  private val codec: Codec[Category] = (id *: nes *: nes *: nes).to[Category]
  val insert: Command[Category] =
    sql"""INSERT INTO categories VALUES ($codec)""".command

  val fetch: Query[Void, Category] =
    sql"""SELECT * FROM categories""".query(codec)
}
