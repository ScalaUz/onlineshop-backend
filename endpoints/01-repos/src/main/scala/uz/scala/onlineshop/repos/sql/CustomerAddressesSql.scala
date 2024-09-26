package uz.scala.onlineshop.repos.sql

import skunk._
import skunk.implicits._
import uz.scala.onlineshop.repos.dto.CustomerAddress
import uz.scala.onlineshop.domain.CustomerId

private[repos] object CustomerAddressesSql extends Sql[CustomerId] {
  private val codec: Codec[CustomerAddress] =
    (id *: CountriesSql.id *: RegionsSql.id *: CitiesSql.id *: nes *: nes *: zdt.opt).to[CustomerAddress]
  val insert: Command[CustomerAddress] =
    sql"""INSERT INTO customers_addresses VALUES ($codec)""".command

  val fetch: Query[Void, CustomerAddress] =
    sql"""SELECT * FROM customers_addresses""".query(codec)
}
