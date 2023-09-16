package onlineshop.repos.sql

import skunk._
import skunk.codec.all.varchar
import skunk.implicits._

import onlineshop.domain.Product
private[repos] object ProductsSql {
  private val codec: Codec[Product] = varchar.to[Product]
  val insert: Command[Product] =
    sql"""INSERT INTO products VALUES ($codec)""".command

  val fetch: Query[Void, Product] =
    sql"""SELECT * FROM products""".query(codec)
}
