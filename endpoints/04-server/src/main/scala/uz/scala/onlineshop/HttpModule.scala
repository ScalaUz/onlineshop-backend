package uz.scala.onlineshop

import cats.data.NonEmptyList
import cats.effect.Async
import cats.effect.kernel.Resource
import cats.effect.std.Dispatcher
import cats.implicits.toSemigroupKOps
import org.http4s.HttpRoutes
import org.http4s.circe.JsonDecoder
import org.http4s.server.Router
import org.http4s.server.Server
import org.typelevel.log4cats.Logger
import uz.scala.http4s.HttpServer
import uz.scala.http4s.utils.Routes
import uz.scala.onlineshop.api.routes.GraphQLRoutes
import uz.scala.onlineshop.domain.AuthedUser
import uz.scala.onlineshop.http.Environment

object HttpModule {
  private def allRoutes[F[_]: Async: JsonDecoder: Dispatcher: Logger](
      env: Environment[F]
    ): NonEmptyList[HttpRoutes[F]] =
    NonEmptyList
      .of[Routes[F, Option[AuthedUser]]](
        new GraphQLRoutes[F](env.algebras)
      )
      .map { r =>
        Router(
          r.path -> (r.public <+> env.middleware(r.`private`))
        )
      }

  def make[F[_]: Async: Dispatcher](
      env: Environment[F]
    )(implicit
      logger: Logger[F]
    ): Resource[F, Server] =
    for {
      _ <- Resource.eval(logger.info(s"Starting HTTP server"))
      server <- HttpServer.make[F](env.config, _ => allRoutes[F](env))
      _ <- Resource.eval(logger.info(s"HTTP server started"))
    } yield server
}
