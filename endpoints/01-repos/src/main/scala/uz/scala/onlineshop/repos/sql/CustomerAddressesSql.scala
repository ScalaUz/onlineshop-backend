package uz.scala.onlineshop.repos.sql

import skunk._
import skunk.implicits._
import uz.scala.onlineshop.domain.CustomerId
import uz.scala.onlineshop.repos.dto.CustomerAddress

private[repos] object CustomerAddressesSql extends Sql[CustomerId] {
  private val codec: Codec[CustomerAddress] =
    (id *: CountriesSql.id *: RegionsSql.id *: CitiesSql.id *: nes *: nes *: zdt.opt)
      .to[CustomerAddress]
  val insert: Command[CustomerAddress] =
    sql"""INSERT INTO customers_addresses VALUES ($codec)""".command

  def selectByIds(ids: List[CustomerId]): Query[ids.type, CustomerAddress] =
    sql"""SELECT * FROM customers_addresses WHERE id IN (${id.values.list(ids)})""".query(codec)
}
