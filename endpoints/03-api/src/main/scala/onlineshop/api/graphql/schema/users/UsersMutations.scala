package onlineshop.api.graphql.schema.users

import caliban.schema.Annotations.GQLName

import onlineshop.algebras.Users
import onlineshop.domain.PersonId
import onlineshop.domain.args.UserInfo

@GQLName("Users")
case class UsersMutations[F[_]](
    create: UserInfo => F[PersonId]
  )

object UsersMutations {
  def make[F[_]](
      usersAlgebra: Users[F]
    ): UsersMutations[F] =
    UsersMutations[F](
      create = userInfo => usersAlgebra.create(userInfo)
    )
}
