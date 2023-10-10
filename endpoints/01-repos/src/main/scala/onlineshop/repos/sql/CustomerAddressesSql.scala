package onlineshop.repos.sql

import skunk._
import skunk.implicits._

import onlineshop.domain.CustomerAddress
import onlineshop.domain.PersonId

private[repos] object CustomerAddressesSql extends Sql[PersonId] {
  private val codec: Codec[CustomerAddress] =
    (id *: CountriesSql.id *: RegionsSql.id *: CitiesSql.id *: nes *: nes).to[CustomerAddress]
  val insert: Command[CustomerAddress] =
    sql"""INSERT INTO customers_addresses VALUES ($codec)""".command

  val fetch: Query[Void, CustomerAddress] =
    sql"""SELECT * FROM customers_addresses""".query(codec)
}
