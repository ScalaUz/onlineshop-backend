package onlineshop.repos.sql

import skunk._
import skunk.implicits._

import onlineshop.domain.Country
import onlineshop.domain.CountryId

private[repos] object CountriesSql extends Sql[CountryId] {
  private val codec: Codec[Country] = (id *: nes *: nes *: nes).to[Country]
  val insert: Command[Country] =
    sql"""INSERT INTO countries VALUES ($codec)""".command

  val fetch: Query[Void, Country] =
    sql"""SELECT * FROM countries""".query(codec)
}
