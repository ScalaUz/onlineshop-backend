package onlineshop.http

import caliban.GraphQL
import cats.effect.Async
import org.http4s.server
import uz.scala.http4s.HttpServerConfig

import onlineshop.api.graphql.GraphQLEndpoints
import onlineshop.domain.AuthedUser

case class Environment[F[_]: Async](
    config: HttpServerConfig,
    middleware: server.AuthMiddleware[F, Option[AuthedUser]],
    graphQL: Option[AuthedUser] => GraphQLEndpoints[F],
  )
