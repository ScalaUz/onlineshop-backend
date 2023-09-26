package onlineshop.api.graphql.schema.market

import caliban.schema.Annotations.GQLName
import onlineshop.algebras.Users
import onlineshop.api.graphql.schema.Utils.Access
import onlineshop.domain.PersonId
import onlineshop.domain.args.UserInfo
import onlineshop.domain.enums.Role

@GQLName("Users")
case class MarketMutations[F[_]](
    @Access(Role.TechAdmin) create: UserInfo => F[PersonId]
  )

object MarketMutations {
  def make[F[_]](
      usersAlgebra: Users[F]
    ): MarketMutations[F] =
    MarketMutations[F](
      create = userInfo => usersAlgebra.create(userInfo)
    )
}
