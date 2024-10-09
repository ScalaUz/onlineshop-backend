package uz.scala.onlineshop.algebras

import cats.Monad
import cats.data.NonEmptyList
import cats.implicits.toFlatMapOps
import cats.implicits.toFunctorOps
import uz.scala.onlineshop.Language
import uz.scala.onlineshop.Phone
import uz.scala.onlineshop.domain.CustomerId
import uz.scala.onlineshop.domain.customers.Customer
import uz.scala.onlineshop.domain.customers.CustomerAddress
import uz.scala.onlineshop.domain.customers.CustomerInput
import uz.scala.onlineshop.domain.customers.CustomerUpdateInput
import uz.scala.onlineshop.effects.Calendar
import uz.scala.onlineshop.effects.GenUUID
import uz.scala.onlineshop.repos.CustomersRepository
import uz.scala.onlineshop.repos.dto
import uz.scala.onlineshop.utils.ID

trait CustomersAlgebra[F[_]] {
  def create(customerInfo: CustomerInput): F[dto.Customer]
  def findByPhone(phone: Phone): F[Option[dto.Customer]]
  def findById(id: CustomerId): F[Option[dto.Customer]]
  def update(customerUpdate: CustomerUpdateInput)(implicit lang: Language): F[Unit]
  def delete(id: CustomerId): F[Unit]
  def getCustomers: F[List[Customer]]
  def getCustomerAddressByIds(
      ids: NonEmptyList[CustomerId]
    ): F[Map[CustomerId, List[CustomerAddress]]]
}

object CustomersAlgebra {
  def make[F[_]: Monad: GenUUID: Calendar](
      customersRepository: CustomersRepository[F]
    ): CustomersAlgebra[F] =
    new CustomersAlgebra[F] {
      override def create(customerInfo: CustomerInput): F[dto.Customer] =
        for {
          id <- ID.make[F, CustomerId]
          now <- Calendar[F].currentZonedDateTime
          customer = dto.Customer(
            id = id,
            createdAt = now,
            name = customerInfo.name,
            phone = customerInfo.phone,
          )

          _ <- customersRepository.create(customer)
        } yield customer

      override def findByPhone(phone: Phone): F[Option[dto.Customer]] =
        customersRepository.find(phone)

      override def findById(id: CustomerId): F[Option[dto.Customer]] =
        customersRepository.findById(id)

      override def update(customerUpdate: CustomerUpdateInput)(implicit lang: Language): F[Unit] =
        customersRepository.update(customerUpdate.id)(_.copy(name = customerUpdate.name))

      override def delete(id: CustomerId): F[Unit] =
        customersRepository.delete(id)

      override def getCustomers: F[List[Customer]] =
        customersRepository.getCustomers.map(_.map(_.toView))

      override def getCustomerAddressByIds(
          ids: NonEmptyList[CustomerId]
        ): F[Map[CustomerId, List[CustomerAddress]]] =
        customersRepository
          .getCustomersAddresses(ids)
          .map(_.view.mapValues(_.map(_.toDomain)).toMap)
    }
}
