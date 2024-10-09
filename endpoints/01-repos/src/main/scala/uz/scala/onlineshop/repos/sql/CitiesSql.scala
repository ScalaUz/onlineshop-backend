package uz.scala.onlineshop.repos.sql

import skunk._
import skunk.implicits._
import uz.scala.onlineshop.repos.dto.City
import uz.scala.onlineshop.domain.CityId

private[repos] object CitiesSql extends Sql[CityId] {
  private val codec: Codec[City] =
    (id *: RegionsSql.id *: nes *: nes *: nes).to[City]
  val insert: Command[City] =
    sql"""INSERT INTO cities VALUES ($codec)""".command

  val fetch: Query[Void, City] =
    sql"""SELECT * FROM cities""".query(codec)

  def selectByIds(ids: List[CityId]): Query[ids.type, City] =
    sql"""SELECT * FROM cities WHERE id IN (${id.values.list(ids)})""".query(codec)
}
