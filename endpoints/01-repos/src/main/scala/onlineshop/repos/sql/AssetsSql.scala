package onlineshop.repos.sql

import skunk._
import skunk.codec.all.bool
import skunk.implicits._

import onlineshop.domain.Asset
import onlineshop.domain.AssetId

private[repos] object AssetsSql extends Sql[AssetId] {
  private val codec: Codec[Asset] = (id *: zonedDateTime *: nes *: bool *: nes.opt).to[Asset]
  val insert: Command[Asset] =
    sql"""INSERT INTO assets VALUES ($codec)""".command

  val fetch: Query[Void, Asset] =
    sql"""SELECT * FROM assets""".query(codec)
}
