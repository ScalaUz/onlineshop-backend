package onlineshop.http

import cats.effect.Async
import org.http4s.server
import uz.scala.http4s.HttpServerConfig

import onlineshop.algebras.Assets
import onlineshop.api.graphql.GraphQLEndpoints
import onlineshop.auth.impl.Auth
import onlineshop.domain.AuthedUser

case class Environment[F[_]: Async](
    config: HttpServerConfig,
    auth: Auth[F, Option[AuthedUser]],
    middleware: server.AuthMiddleware[F, Option[AuthedUser]],
    graphQL: Option[AuthedUser] => GraphQLEndpoints[F],
    algebras: Environment.Algebras[F],
  )

object Environment {
  case class Algebras[F[_]](
      assets: Assets[F]
    )
}
