package uz.scala.onlineshop.repos

import cats.data.NonEmptyList
import cats.effect.Async
import cats.effect.Resource
import cats.implicits.toFunctorOps
import skunk._
import uz.scala.onlineshop.domain.CityId
import uz.scala.onlineshop.domain.CountryId
import uz.scala.onlineshop.domain.RegionId
import uz.scala.onlineshop.repos.dto.City
import uz.scala.onlineshop.repos.dto.Country
import uz.scala.onlineshop.repos.dto.Region
import uz.scala.onlineshop.repos.sql.CitiesSql
import uz.scala.onlineshop.repos.sql.CountriesSql
import uz.scala.onlineshop.repos.sql.RegionsSql
import uz.scala.skunk.syntax.all.skunkSyntaxQueryOps

trait AddressesRepository[F[_]] {
  def getCountryByIds(ids: NonEmptyList[CountryId]): F[Map[CountryId, Country]]
  def getRegionByIds(ids: NonEmptyList[RegionId]): F[Map[RegionId, Region]]
  def getCityByIds(ids: NonEmptyList[CityId]): F[Map[CityId, City]]
}

object AddressesRepository {
  def make[F[_]: Async](
      implicit
      session: Resource[F, Session[F]]
    ): AddressesRepository[F] = new AddressesRepository[F] {
    override def getCityByIds(
        ids: NonEmptyList[CityId]
      ): F[Map[CityId, City]] = {
      val listIds = ids.toList
      CitiesSql.selectByIds(listIds).queryList(listIds).map(_.map(c => c.id -> c).toMap)
    }

    override def getCountryByIds(
        ids: NonEmptyList[CountryId]
      ): F[Map[CountryId, Country]] = {
      val listIds = ids.toList
      CountriesSql.selectByIds(listIds).queryList(listIds).map(_.map(c => c.id -> c).toMap)
    }

    override def getRegionByIds(
        ids: NonEmptyList[RegionId]
      ): F[Map[RegionId, Region]] = {
      val listIds = ids.toList
      RegionsSql.selectByIds(listIds).queryList(listIds).map(_.map(c => c.id -> c).toMap)
    }
  }
}
