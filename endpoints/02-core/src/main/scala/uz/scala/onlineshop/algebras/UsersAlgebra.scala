package uz.scala.onlineshop.algebras

import cats.Monad
import cats.effect.std.Random
import cats.implicits.toFlatMapOps
import cats.implicits.toFunctorOps
import tsec.passwordhashers.PasswordHasher
import tsec.passwordhashers.jca.SCrypt
import uz.scala.onlineshop.EmailAddress
import uz.scala.onlineshop.Language
import uz.scala.onlineshop.domain.AuthedUser.User
import uz.scala.onlineshop.domain.UserId
import uz.scala.onlineshop.domain.users.UserInput
import uz.scala.onlineshop.domain.users.UserUpdateInput
import uz.scala.onlineshop.effects.Calendar
import uz.scala.onlineshop.effects.GenUUID
import uz.scala.onlineshop.randomStr
import uz.scala.onlineshop.repos.UsersRepository
import uz.scala.onlineshop.repos.dto
import uz.scala.onlineshop.utils.ID

trait UsersAlgebra[F[_]] {
  def create(userInfo: UserInput): F[UserId]
  def findById(id: UserId): F[Option[dto.User]]
  def getUsers: F[List[User]]
  def findByEmail(email: EmailAddress): F[Option[dto.User]]
  def update(userUpdate: UserUpdateInput)(implicit lang: Language): F[Unit]
  def delete(id: UserId): F[Unit]
}

object UsersAlgebra {
  def make[F[_]: Monad: GenUUID: Calendar: Random](
      usersRepository: UsersRepository[F]
    )(implicit
      P: PasswordHasher[F, SCrypt]
    ): UsersAlgebra[F] =
    new UsersAlgebra[F] {
      override def create(userInfo: UserInput): F[UserId] =
        for {
          id <- ID.make[F, UserId]
          now <- Calendar[F].currentZonedDateTime
          passwordStr <- randomStr[F](6)
          hash <- SCrypt.hashpw[F](passwordStr)

          user = dto.User(
            id = id,
            createdAt = now,
            name = userInfo.name,
            email = userInfo.email,
            password = hash,
          )

          _ <- usersRepository.create(user)
        } yield id

      override def findById(id: UserId): F[Option[dto.User]] =
        usersRepository.findById(id)

      override def findByEmail(email: EmailAddress): F[Option[dto.User]] =
        usersRepository.find(email)

      override def getUsers: F[List[User]] =
        usersRepository.getUsers.map(_.map(_.toDomain))

      override def update(userUpdate: UserUpdateInput)(implicit lang: Language): F[Unit] =
        usersRepository.update(userUpdate.id)(_.copy(name = userUpdate.name))

      override def delete(id: UserId): F[Unit] =
        usersRepository.delete(id)
    }
}
