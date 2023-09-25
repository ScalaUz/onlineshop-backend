package onlineshop.repos

import cats.effect.Async
import cats.effect.Resource
import skunk._
import uz.scala.skunk.syntax.all.skunkSyntaxCommandOps
import uz.scala.skunk.syntax.all.skunkSyntaxQueryOps

import onlineshop.Phone
import onlineshop.domain.AccessCredentials
import onlineshop.domain.AuthedUser.Customer
import onlineshop.repos.sql.CustomersSql
trait CustomersRepository[F[_]] {
  def find(phone: Phone): F[Option[AccessCredentials[Customer]]]
  def create(userAndHash: AccessCredentials[Customer]): F[Unit]
}

object CustomersRepository {
  def make[F[_]: Async](
      implicit
      session: Resource[F, Session[F]]
    ): CustomersRepository[F] = new CustomersRepository[F] {
    override def find(phone: Phone): F[Option[AccessCredentials[Customer]]] =
      CustomersSql.findByPhone.queryOption(phone)

    override def create(userAndHash: AccessCredentials[Customer]): F[Unit] =
      CustomersSql.insert.execute(userAndHash)
  }
}
