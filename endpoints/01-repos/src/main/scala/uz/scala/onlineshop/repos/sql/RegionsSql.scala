package uz.scala.onlineshop.repos.sql

import skunk._
import skunk.implicits._
import uz.scala.onlineshop.domain.RegionId
import uz.scala.onlineshop.repos.dto.Region

private[repos] object RegionsSql extends Sql[RegionId] {
  private val codec: Codec[Region] =
    (id *: CountriesSql.id *: nes *: nes *: nes).to[Region]
  val insert: Command[Region] =
    sql"""INSERT INTO regions VALUES ($codec)""".command

  val fetch: Query[Void, Region] =
    sql"""SELECT * FROM regions""".query(codec)

  def selectByIds(ids: List[RegionId]): Query[ids.type, Region] =
    sql"""SELECT * FROM regions WHERE id IN (${id.values.list(ids)})""".query(codec)
}
