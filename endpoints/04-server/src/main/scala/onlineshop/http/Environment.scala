package onlineshop.http

import cats.effect.Async
import uz.scala.http4s.HttpServerConfig

import onlineshop.api.graphql.GraphQL
case class Environment[F[_]: Async](
    config: HttpServerConfig,
    graphQL: GraphQL[F],
  )
