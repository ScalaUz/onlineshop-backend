package uz.scala.onlineshop.repos.sql

import skunk._
import skunk.implicits._
import uz.scala.onlineshop.domain.BrandId
import uz.scala.onlineshop.repos.dto.Brand

private[repos] object BrandsSql extends Sql[BrandId] {
  private val codec: Codec[Brand] = (id *: nes *: AssetsSql.id *: zdt.opt *: zdt.opt).to[Brand]
  val insert: Command[Brand] =
    sql"""INSERT INTO brands VALUES ($codec)""".command

  val fetch: Query[Void, Brand] =
    sql"""SELECT * FROM brands WHERE deleted_at IS NULL""".query(codec)

  val fetchById: Query[BrandId, Brand] =
    sql"""SELECT * FROM brands WHERE id = $id AND deleted_at IS NULL LIMIT 1""".query(codec)

  val update: Command[Brand] =
    sql"""UPDATE
           brands
          SET name = $nes,
           asset_id = ${AssetsSql.id}
           updated_at = now()
          WHERE id = $id"""
      .command
      .contramap { brand: Brand =>
        brand.name *: brand.assetId *: brand.id *: EmptyTuple
      }

  val delete: Command[BrandId] =
    sql"""UPDATE
           brands
          SET deleted_at = now()
          WHERE id = $id""".command
}
