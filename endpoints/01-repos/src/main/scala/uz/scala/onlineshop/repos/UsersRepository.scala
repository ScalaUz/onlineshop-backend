package uz.scala.onlineshop.repos

import cats.data.OptionT
import cats.effect.Async
import cats.effect.Resource
import skunk._
import uz.scala.onlineshop.EmailAddress
import uz.scala.onlineshop.domain.UserId
import uz.scala.onlineshop.exception.AError
import uz.scala.onlineshop.repos.dto.User
import uz.scala.onlineshop.repos.sql.UsersSql
import uz.scala.skunk.syntax.all._

trait UsersRepository[F[_]] {
  def find(email: EmailAddress): F[Option[User]]
  def create(userAndHash: User): F[Unit]
  def findById(id: UserId): F[Option[User]]
  def update(id: UserId)(update: User => User): F[Unit]
  def delete(id: UserId): F[Unit]
}

object UsersRepository {
  def make[F[_]: Async](
      implicit
      session: Resource[F, Session[F]]
    ): UsersRepository[F] = new UsersRepository[F] {
    override def find(email: EmailAddress): F[Option[User]] =
      UsersSql.findByLogin.queryOption(email)

    override def create(user: User): F[Unit] =
      UsersSql.insert.execute(user)

    override def findById(id: UserId): F[Option[User]] =
      UsersSql.findById.queryOption(id)

    override def update(id: UserId)(update: User => User): F[Unit] =
      OptionT(findById(id))
        .semiflatMap(u => UsersSql.update.execute(update(u)))
        .getOrRaise(AError.Internal("User not found"))

    override def delete(id: UserId): F[Unit] =
      UsersSql.delete.execute(id)
  }
}
