package onlineshop.algebras

import cats.Monad
import cats.implicits.toFlatMapOps
import cats.implicits.toFunctorOps
import tsec.passwordhashers.PasswordHasher
import tsec.passwordhashers.jca.SCrypt

import onlineshop.domain.AccessCredentials
import onlineshop.domain.AuthedUser.Customer
import onlineshop.domain.PersonId
import onlineshop.domain.args.CustomerInfo
import onlineshop.effects.Calendar
import onlineshop.effects.GenUUID
import onlineshop.repos.CustomersRepository
import onlineshop.utils.ID

trait CustomersAlgebra[F[_]] {
  def create(customerInfo: CustomerInfo): F[PersonId]
}

object CustomersAlgebra {
  def make[F[_]: Monad: GenUUID: Calendar](
      customersRepository: CustomersRepository[F]
    )(implicit
      P: PasswordHasher[F, SCrypt]
    ): CustomersAlgebra[F] =
    new CustomersAlgebra[F] {
      override def create(customerInfo: CustomerInfo): F[PersonId] =
        for {
          id <- ID.make[F, PersonId]
          now <- Calendar[F].currentZonedDateTime
          customer = Customer(
            id = id,
            createdAt = now,
            firstname = customerInfo.firstname,
            lastname = customerInfo.lastname,
            phone = customerInfo.phone,
          )

          hash <- SCrypt.hashpw[F](customerInfo.password.value)

          accessCredentials = AccessCredentials(customer, hash)
          _ <- customersRepository.create(accessCredentials)
        } yield id
    }
}
