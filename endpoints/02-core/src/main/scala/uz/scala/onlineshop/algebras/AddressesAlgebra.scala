package uz.scala.onlineshop.algebras

import cats.Monad
import cats.data.NonEmptyList
import cats.implicits.toFunctorOps
import uz.scala.onlineshop.Language
import uz.scala.onlineshop.domain.City
import uz.scala.onlineshop.domain.CityId
import uz.scala.onlineshop.domain.Country
import uz.scala.onlineshop.domain.CountryId
import uz.scala.onlineshop.domain.Region
import uz.scala.onlineshop.domain.RegionId
import uz.scala.onlineshop.repos.AddressesRepository

trait AddressesAlgebra[F[_]] {
  def getCountryByIds(
      ids: NonEmptyList[CountryId]
    )(implicit
      lang: Language
    ): F[Map[CountryId, Country]]
  def getRegionByIds(ids: NonEmptyList[RegionId])(implicit lang: Language): F[Map[RegionId, Region]]
  def getCityByIds(ids: NonEmptyList[CityId])(implicit lang: Language): F[Map[CityId, City]]
}
object AddressesAlgebra {
  def make[F[_]: Monad](
      addressesRepository: AddressesRepository[F]
    ): AddressesAlgebra[F] =
    new AddressesAlgebra[F] {
      override def getCountryByIds(
          ids: NonEmptyList[CountryId]
        )(implicit
          lang: Language
        ): F[Map[CountryId, Country]] =
        addressesRepository.getCountryByIds(ids).map(_.view.mapValues(_.toDomain).toMap)

      override def getRegionByIds(
          ids: NonEmptyList[RegionId]
        )(implicit
          lang: Language
        ): F[Map[RegionId, Region]] =
        addressesRepository.getRegionByIds(ids).map(_.view.mapValues(_.toDomain).toMap)

      override def getCityByIds(
          ids: NonEmptyList[CityId]
        )(implicit
          lang: Language
        ): F[Map[CityId, City]] =
        addressesRepository.getCityByIds(ids).map(_.view.mapValues(_.toDomain).toMap)
    }
}
