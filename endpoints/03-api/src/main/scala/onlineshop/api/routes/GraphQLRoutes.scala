package onlineshop.api.routes

import caliban.CalibanError
import caliban.GraphQL
import caliban.Http4sAdapter
import caliban.interop.cats.CatsInterop
import caliban.interop.tapir.HttpInterpreter
import cats.effect.Async
import cats.implicits.toFunctorOps
import org.http4s._
import org.http4s.dsl._
import org.http4s.server.Router
import sttp.tapir.json.circe._

import onlineshop.api.graphql.GraphQLContext

final case class GraphQLRoutes[F[_]: Async](
    graphQL: GraphQL[GraphQLContext[F]]
  )(implicit
    interop: CatsInterop[F, GraphQLContext[F]]
  ) extends Http4sDsl[F] {
  val routes: F[HttpRoutes[F]] =
    for {
      interpreter <- interop.toEffect(graphQL.interpreter)
      httpRoutes = Http4sAdapter
        .makeHttpServiceF[F, GraphQLContext[F], CalibanError](
          HttpInterpreter(interpreter)
        )
    } yield Router("graphql" -> httpRoutes)
}
