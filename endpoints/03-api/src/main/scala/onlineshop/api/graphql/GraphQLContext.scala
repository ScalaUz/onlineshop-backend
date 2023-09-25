package onlineshop.api.graphql

import onlineshop.domain.AuthedUser

case class GraphQLContext(
    authInfo: Option[AuthedUser] = None
  )
