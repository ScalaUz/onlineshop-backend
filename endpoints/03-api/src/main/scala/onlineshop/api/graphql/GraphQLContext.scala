package onlineshop.api.graphql

import caliban.uploads.Uploads
import zio.ULayer

import onlineshop.domain.AuthedUser

case class GraphQLContext(
    authInfo: Option[AuthedUser],
    uploads: ULayer[Uploads],
  )

object GraphQLContext {
  val empty: GraphQLContext = GraphQLContext(None, Uploads.empty)
}
