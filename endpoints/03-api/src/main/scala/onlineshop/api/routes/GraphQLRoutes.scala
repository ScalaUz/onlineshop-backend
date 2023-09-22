package onlineshop.api.routes

import caliban.GraphQLRequest
import cats.effect.Async
import cats.implicits.toFlatMapOps
import cats.implicits.toFunctorOps
import org.http4s.AuthedRoutes
import org.http4s._
import org.http4s.circe.CirceEntityCodec._
import uz.scala.http4s.utils.Routes

import onlineshop.api.graphql.GraphQLEndpoints
import onlineshop.domain.AuthedUser

final case class GraphQLRoutes[F[_]: Async](
    graphQL: Option[AuthedUser] => GraphQLEndpoints[F]
  ) extends Routes[F, Option[AuthedUser]] {
  override val path: String = "/"
  override val public: HttpRoutes[F] = HttpRoutes.empty
  override val `private`: AuthedRoutes[Option[AuthedUser], F] =
    AuthedRoutes.of[Option[AuthedUser], F] {
      case ar @ POST -> Root / "graphql" as authedUser =>
        for {
          interpreter <- graphQL(authedUser)
            .interop
            .toEffect(graphQL(authedUser).createGraphQL.interpreter)
          query <- ar.req.as[GraphQLRequest]
          result <- graphQL(authedUser).interop.toEffect(interpreter.executeRequest(query))
          response <- Ok(result)
        } yield response
    }
}
