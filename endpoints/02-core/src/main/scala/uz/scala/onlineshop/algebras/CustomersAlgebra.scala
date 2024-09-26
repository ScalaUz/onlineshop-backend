package uz.scala.onlineshop.algebras

import cats.Monad
import cats.implicits.toFlatMapOps
import cats.implicits.toFunctorOps
import uz.scala.onlineshop.Phone
import uz.scala.onlineshop.domain.CustomerId
import uz.scala.onlineshop.domain.customers.Customer
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
  def update(id: CustomerId, customerUpdate: CustomerUpdateInput): F[Unit]
  def delete(id: CustomerId): F[Unit]
  def getCustomers: F[List[Customer]]
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

      override def update(id: CustomerId, customerUpdate: CustomerUpdateInput): F[Unit] =
        customersRepository.update(id)(_.copy(name = customerUpdate.name))

      override def delete(id: CustomerId): F[Unit] =
        customersRepository.delete(id)

      override def getCustomers: F[List[Customer]] =
        customersRepository.getCustomers.map(_.map(_.toView))
    }
}
