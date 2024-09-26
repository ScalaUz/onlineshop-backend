package uz.scala.onlineshop.repos.sql

import skunk._
import skunk.implicits._
import uz.scala.onlineshop.domain.{Asset, AssetId}

private[repos] object AssetsSql extends Sql[AssetId] {
  private val codec: Codec[Asset] = (id *: zdt *: nes *: nes.opt *: nes.opt).to[Asset]
  val insert: Command[Asset] =
    sql"""INSERT INTO assets VALUES ($codec)""".command

  val findById: Query[AssetId, Asset] =
    sql"""SELECT * FROM assets WHERE id = $id LIMIT 1""".query(codec)

  def findByIds(list: List[AssetId]): Query[list.type, Asset] =
    sql"""SELECT * FROM assets WHERE id IN (${id.values.list(list)})""".query(codec)
}
