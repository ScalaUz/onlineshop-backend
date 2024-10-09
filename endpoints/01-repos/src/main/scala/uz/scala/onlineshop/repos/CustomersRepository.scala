package uz.scala.onlineshop.repos

import cats.data.NonEmptyList
import cats.data.OptionT
import cats.effect.Async
import cats.effect.Resource
import cats.implicits.toFunctorOps
import skunk.Session
import skunk.Void
import uz.scala.onlineshop.Language
import uz.scala.onlineshop.Phone
import uz.scala.onlineshop.ResponseMessages.USER_NOT_FOUND
import uz.scala.onlineshop.domain.CustomerId
import uz.scala.onlineshop.exception.AError
import uz.scala.onlineshop.repos.dto.Customer
import uz.scala.onlineshop.repos.dto.CustomerAddress
import uz.scala.onlineshop.repos.sql.CustomerAddressesSql
import uz.scala.onlineshop.repos.sql.CustomersSql
import uz.scala.skunk.syntax.all.skunkSyntaxCommandOps
import uz.scala.skunk.syntax.all.skunkSyntaxQueryOps

trait CustomersRepository[F[_]] {
  def find(phone: Phone): F[Option[Customer]]
  def create(customer: Customer): F[Unit]
  def findById(id: CustomerId): F[Option[Customer]]
  def update(id: CustomerId)(update: Customer => Customer)(implicit lang: Language): F[Unit]
  def delete(id: CustomerId): F[Unit]
  def getCustomers: F[List[Customer]]
  def getCustomersAddresses(
      customerIds: NonEmptyList[CustomerId]
    ): F[Map[CustomerId, List[CustomerAddress]]]
}

object CustomersRepository {
  def make[F[_]: Async](
      implicit
      session: Resource[F, Session[F]]
    ): CustomersRepository[F] = new CustomersRepository[F] {
    override def find(phone: Phone): F[Option[Customer]] =
      CustomersSql.findByLogin.queryOption(phone)

    override def create(customer: Customer): F[Unit] =
      CustomersSql.insert.execute(customer)

    override def findById(id: CustomerId): F[Option[Customer]] =
      CustomersSql.findById.queryOption(id)

    override def update(
        id: CustomerId
      )(
        update: Customer => Customer
      )(implicit
        lang: Language
      ): F[Unit] =
      OptionT(findById(id))
        .semiflatMap(c => CustomersSql.update.execute(update(c)))
        .getOrRaise(AError.Internal(USER_NOT_FOUND(lang)))

    override def delete(id: CustomerId): F[Unit] =
      CustomersSql.delete.execute(id)

    override def getCustomers: F[List[Customer]] =
      CustomersSql.select.queryList(Void)

    override def getCustomersAddresses(
        customerIds: NonEmptyList[CustomerId]
      ): F[Map[CustomerId, List[CustomerAddress]]] = {
      val listIds = customerIds.toList
      CustomerAddressesSql
        .selectByIds(listIds)
        .queryList(listIds)
        .map(_.groupBy(_.customerId))
    }
  }
}
