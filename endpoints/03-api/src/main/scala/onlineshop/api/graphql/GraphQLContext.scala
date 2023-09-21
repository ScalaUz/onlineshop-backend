package onlineshop.api.graphql

import onlineshop.Algebras
import onlineshop.domain.AuthInfo

case class GraphQLContext[F[_]](
    authInfo: Option[AuthInfo] = None,
    algebras: Algebras[F],
  )
