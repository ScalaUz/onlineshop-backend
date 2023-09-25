package onlineshop.repos

import cats.effect.Async
import cats.effect.Resource
import skunk._
import uz.scala.skunk.syntax.all._

import onlineshop.Phone
import onlineshop.domain.AccessCredentials
import onlineshop.domain.AuthedUser.User
import onlineshop.repos.sql.UsersSql
trait UsersRepository[F[_]] {
  def find(phone: Phone): F[Option[AccessCredentials[User]]]
  def create(userAndHash: AccessCredentials[User]): F[Unit]
}

object UsersRepository {
  def make[F[_]: Async](
      implicit
      session: Resource[F, Session[F]]
    ): UsersRepository[F] = new UsersRepository[F] {
    override def find(phone: Phone): F[Option[AccessCredentials[User]]] =
      UsersSql.findByLogin.queryOption(phone)

    override def create(userAndHash: AccessCredentials[User]): F[Unit] =
      UsersSql.insert.execute(userAndHash)
  }
}
