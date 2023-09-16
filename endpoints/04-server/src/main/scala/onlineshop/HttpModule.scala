package onlineshop

import cats.data.NonEmptyList
import cats.effect.Async
import cats.effect.ExitCode
import cats.effect.kernel.Resource
import cats.implicits.toFunctorOps
import org.http4s.HttpRoutes
import org.http4s.circe.JsonDecoder
import org.typelevel.log4cats.Logger
import uz.scala.http4s.HttpServer

import onlineshop.api.graphql.GraphQL
import onlineshop.api.routes.GraphQLRoutes
import onlineshop.http.Environment

object HttpModule {
  private def allRoutes[F[_]: Async: JsonDecoder: Logger](
      graphQL: GraphQL[F]
    ): NonEmptyList[HttpRoutes[F]] =
    NonEmptyList
      .of[HttpRoutes[F]](
        new GraphQLRoutes[F](graphQL).routes
      )

  def make[F[_]: Async](
      env: Environment[F]
    )(implicit
      logger: Logger[F]
    ): Resource[F, F[ExitCode]] =
    HttpServer.make[F](env.config, _ => allRoutes[F](env.graphQL)).map { _ =>
      logger.info(s"OnlineShop http server is started").as(ExitCode.Success)
    }
}
