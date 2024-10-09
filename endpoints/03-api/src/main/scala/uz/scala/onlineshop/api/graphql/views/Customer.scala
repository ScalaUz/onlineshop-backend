package uz.scala.onlineshop.api.graphql.views

import caliban.interop.cats.CatsInterop
import cats.Applicative
import eu.timepit.refined.types.string.NonEmptyString
import io.scalaland.chimney.dsl.TransformationOps
import uz.scala.onlineshop.Language
import uz.scala.onlineshop.Phone
import uz.scala.onlineshop.algebras.AddressesAlgebra
import uz.scala.onlineshop.algebras.CustomersAlgebra
import uz.scala.onlineshop.api.graphql.DataFetcher
import uz.scala.onlineshop.api.graphql.GraphQLContext
import uz.scala.onlineshop.domain
import uz.scala.onlineshop.domain.CustomerId

case class Customer(
    id: CustomerId,
    name: NonEmptyString,
    phone: Phone,
    createdAt: java.time.ZonedDateTime,
    addresses: ConsoleQuery[List[CustomerAddress]],
  )

object Customer {
  def fromDomain[F[_]: Applicative](
      address: domain.customers.Customer
    )(implicit
      addressesAlgebra: AddressesAlgebra[F],
      customersAlgebra: CustomersAlgebra[F],
      lang: Language,
      interop: CatsInterop[F, GraphQLContext],
    ): Customer = {
    def getAddressById(id: CustomerId): ConsoleQuery[List[CustomerAddress]] =
      DataFetcher
        .fetch[F, CustomerId, List[domain.customers.CustomerAddress]](
          id,
          customersAlgebra.getCustomerAddressByIds,
        )
        .map(_.map(CustomerAddress.fromDomain[F]))

    address
      .into[Customer]
      .withFieldConst(_.addresses, getAddressById(address.id))
      .transform
  }
}
