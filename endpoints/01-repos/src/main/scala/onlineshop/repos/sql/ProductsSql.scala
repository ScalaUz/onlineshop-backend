package onlineshop.repos.sql

import skunk._
import skunk.implicits._

import onlineshop.domain.Product
import onlineshop.domain.ProductId
private[repos] object ProductsSql extends Sql[ProductId] {
  private val codec: Codec[Product] =
    (id *: zonedDateTime *: nes *: nes *: price *: nes *: nes *: nes *: BrandsSql.id *: CategoriesSql.id)
      .to[Product]
  val insert: Command[Product] =
    sql"""INSERT INTO products VALUES ($codec)""".command

  val fetch: Query[Void, Product] =
    sql"""SELECT * FROM products""".query(codec)
}
