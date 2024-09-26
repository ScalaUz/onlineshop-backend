package uz.scala.onlineshop.repos.sql

import skunk._
import skunk.implicits._
import uz.scala.onlineshop.domain.Region
import uz.scala.onlineshop.domain.RegionId

private[repos] object RegionsSql extends Sql[RegionId] {
  private val codec: Codec[Region] =
    (id *: CountriesSql.id *: nes *: nes *: nes).to[Region]
  val insert: Command[Region] =
    sql"""INSERT INTO regions VALUES ($codec)""".command

  val fetch: Query[Void, Region] =
    sql"""SELECT * FROM regions""".query(codec)
}
