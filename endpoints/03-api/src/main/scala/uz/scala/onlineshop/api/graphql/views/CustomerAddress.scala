package uz.scala.onlineshop.api.graphql.views

import caliban.interop.cats.CatsInterop
import caliban.interop.cats.CatsInterop.materialize
import cats.Applicative
import eu.timepit.refined.types.string.NonEmptyString
import io.scalaland.chimney.dsl.TransformationOps
import uz.scala.onlineshop.Language
import uz.scala.onlineshop.algebras.AddressesAlgebra
import uz.scala.onlineshop.algebras.CustomersAlgebra
import uz.scala.onlineshop.api.graphql.DataFetcher
import uz.scala.onlineshop.api.graphql.GraphQLContext
import uz.scala.onlineshop.domain
import uz.scala.onlineshop.domain.City
import uz.scala.onlineshop.domain.CityId
import uz.scala.onlineshop.domain.Country
import uz.scala.onlineshop.domain.CountryId
import uz.scala.onlineshop.domain.Region
import uz.scala.onlineshop.domain.RegionId

case class CustomerAddress(
    country: ConsoleQuery[Country],
    region: ConsoleQuery[Region],
    city: ConsoleQuery[City],
    street: NonEmptyString,
    postalCode: NonEmptyString,
  )

object CustomerAddress {
  def fromDomain[F[_]: Applicative](
      address: domain.customers.CustomerAddress
    )(implicit
      addressesAlgebra: AddressesAlgebra[F],
      lang: Language,
      interop: CatsInterop[F, GraphQLContext],
    ): CustomerAddress = {
    def getCountryById(id: CountryId): ConsoleQuery[Country] =
      DataFetcher
        .fetch[F, CountryId, Country](id, addressesAlgebra.getCountryByIds)

    def getRegionById(id: RegionId): ConsoleQuery[Region] =
      DataFetcher
        .fetch[F, RegionId, Region](id, addressesAlgebra.getRegionByIds)

    def getCityById(id: CityId): ConsoleQuery[City] =
      DataFetcher
        .fetch[F, CityId, City](id, addressesAlgebra.getCityByIds)

    address
      .into[CustomerAddress]
      .withFieldConst(_.country, getCountryById(address.countryId))
      .withFieldConst(_.region, getRegionById(address.regionId))
      .withFieldConst(_.city, getCityById(address.cityId))
      .transform
  }
}
