package onlineshop.algebras

import cats.Monad
import cats.implicits.toFlatMapOps
import cats.implicits.toFunctorOps
import tsec.passwordhashers.PasswordHasher
import tsec.passwordhashers.jca.SCrypt

import onlineshop.domain.AccessCredentials
import onlineshop.domain.AuthedUser.User
import onlineshop.domain.PersonId
import onlineshop.domain.args.UserInfo
import onlineshop.effects.Calendar
import onlineshop.effects.GenUUID
import onlineshop.repos.UsersRepository
import onlineshop.utils.ID

trait UsersAlgebra[F[_]] {
  def create(userInfo: UserInfo): F[PersonId]
}

object UsersAlgebra {
  def make[F[_]: Monad: GenUUID: Calendar](
      usersRepository: UsersRepository[F]
    )(implicit
      P: PasswordHasher[F, SCrypt]
    ): UsersAlgebra[F] =
    new UsersAlgebra[F] {
      override def create(userInfo: UserInfo): F[PersonId] =
        for {
          id <- ID.make[F, PersonId]
          now <- Calendar[F].currentZonedDateTime
          user = User(
            id = id,
            createdAt = now,
            firstname = userInfo.firstname,
            lastname = userInfo.lastname,
            phone = userInfo.phone,
            role = userInfo.role,
          )

          hash <- SCrypt.hashpw[F](userInfo.password.value)

          accessCredentials = AccessCredentials(user, hash)
          _ <- usersRepository.create(accessCredentials)
        } yield id
    }
}
