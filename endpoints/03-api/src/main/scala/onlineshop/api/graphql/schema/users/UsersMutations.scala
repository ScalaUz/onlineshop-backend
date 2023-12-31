package onlineshop.api.graphql.schema.users

import caliban.schema.Annotations.GQLName
import onlineshop.algebras.Users
import onlineshop.api.graphql.schema.Utils.Access
import onlineshop.domain.PersonId
import onlineshop.domain.args.UserInfo
import onlineshop.domain.enums.Role

@GQLName("Users")
case class UsersMutations[F[_]](
    @Access(Role.TechAdmin) create: UserInfo => F[PersonId]
  )

object UsersMutations {
  def make[F[_]](
      usersAlgebra: Users[F]
    ): UsersMutations[F] =
    UsersMutations[F](
      create = userInfo => usersAlgebra.create(userInfo)
    )
}
