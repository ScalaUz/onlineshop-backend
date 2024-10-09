package uz.scala.onlineshop.repos.sql

import skunk._
import skunk.implicits._
import uz.scala.onlineshop.domain.CountryId
import uz.scala.onlineshop.repos.dto.Country

private[repos] object CountriesSql extends Sql[CountryId] {
  private val codec: Codec[Country] = (id *: nes *: nes *: nes).to[Country]
  val insert: Command[Country] =
    sql"""INSERT INTO countries VALUES ($codec)""".command

  val fetch: Query[Void, Country] =
    sql"""SELECT * FROM countries""".query(codec)

  def selectByIds(ids: List[CountryId]): Query[ids.type, Country] =
    sql"""SELECT * FROM countries WHERE id IN (${id.values.list(ids)})""".query(codec)
}
