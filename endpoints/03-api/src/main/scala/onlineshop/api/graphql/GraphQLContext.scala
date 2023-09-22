package onlineshop.api.graphql

import onlineshop.Algebras
import onlineshop.domain.AuthedUser

case class GraphQLContext[F[_]](
    authInfo: Option[AuthedUser] = None,
    algebras: Algebras[F],
  )
