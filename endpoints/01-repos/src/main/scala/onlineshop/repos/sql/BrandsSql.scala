package onlineshop.repos.sql

import skunk._
import skunk.implicits._

import onlineshop.domain.Brand
import onlineshop.domain.BrandId

private[repos] object BrandsSql extends Sql[BrandId] {
  private val codec: Codec[Brand] = (id *: nes).to[Brand]
  val insert: Command[Brand] =
    sql"""INSERT INTO brands VALUES ($codec)""".command

  val fetch: Query[Void, Brand] =
    sql"""SELECT * FROM brands""".query(codec)
}
