package onlineshop.http

import caliban.GraphQL
import cats.effect.Async
import uz.scala.http4s.HttpServerConfig

import onlineshop.api.graphql.GraphQLContext

case class Environment[F[_]: Async](
    config: HttpServerConfig,
    graphQL: GraphQL[GraphQLContext[F]],
  )
