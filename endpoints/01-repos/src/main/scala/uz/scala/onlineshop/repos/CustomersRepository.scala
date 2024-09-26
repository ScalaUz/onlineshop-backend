package uz.scala.onlineshop.repos

import cats.data.OptionT
import cats.effect.Async
import cats.effect.Resource
import skunk.Session
import skunk.Void
import uz.scala.onlineshop.Phone
import uz.scala.onlineshop.domain.CustomerId
import uz.scala.onlineshop.exception.AError
import uz.scala.onlineshop.repos.dto.Customer
import uz.scala.onlineshop.repos.sql.CustomersSql
import uz.scala.skunk.syntax.all.skunkSyntaxCommandOps
import uz.scala.skunk.syntax.all.skunkSyntaxQueryOps

trait CustomersRepository[F[_]] {
  def find(phone: Phone): F[Option[Customer]]
  def create(customer: Customer): F[Unit]
  def findById(id: CustomerId): F[Option[Customer]]
  def update(id: CustomerId)(update: Customer => Customer): F[Unit]
  def delete(id: CustomerId): F[Unit]
  def getCustomers: F[List[Customer]]
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

    override def update(id: CustomerId)(update: Customer => Customer): F[Unit] =
      OptionT(findById(id))
        .semiflatMap(c => CustomersSql.update.execute(update(c)))
        .getOrRaise(AError.Internal("Customer not found"))

    override def delete(id: CustomerId): F[Unit] =
      CustomersSql.delete.execute(id)

    override def getCustomers: F[List[Customer]] =
      CustomersSql.select.queryList(Void)
  }
}
