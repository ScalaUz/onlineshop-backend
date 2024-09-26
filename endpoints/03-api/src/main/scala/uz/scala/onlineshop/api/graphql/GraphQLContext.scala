package uz.scala.onlineshop.api.graphql

import caliban.uploads.Uploads
import uz.scala.onlineshop.Language
import uz.scala.onlineshop.domain.AuthedUser
import zio.ULayer

case class GraphQLContext(
    authInfo: Option[AuthedUser],
    uploads: ULayer[Uploads],
  )(implicit
    val language: Language
  )

object GraphQLContext {
  val empty: GraphQLContext = GraphQLContext(None, Uploads.empty)(Language.En)
}
