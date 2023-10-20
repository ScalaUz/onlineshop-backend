package onlineshop

import cats.data.NonEmptyList
import cats.effect.Async
import cats.effect.ExitCode
import cats.effect.kernel.Resource
import cats.implicits.toFunctorOps
import cats.implicits.toSemigroupKOps
import org.http4s.HttpRoutes
import org.http4s.server.Router
import org.typelevel.log4cats.Logger
import uz.scala.http4s.HttpServer
import uz.scala.http4s.utils.Routes

import onlineshop.api.routes._
import onlineshop.domain.AuthedUser
import onlineshop.http.Environment

object HttpModule {
  private def allRoutes[F[_]: Async: Logger](
      env: Environment[F]
    ): NonEmptyList[HttpRoutes[F]] =
    NonEmptyList
      .of[Routes[F, Option[AuthedUser]]](
        new RootRoutes[F](env.graphQL, env.algebras.assets),
        new AuthRoutes[F](env.auth),
      )
      .map { r =>
        Router(
          r.path -> (r.public <+> env.middleware(r.`private`))
        )
      }

  def make[F[_]: Async](
      env: Environment[F]
    )(implicit
      logger: Logger[F]
    ): Resource[F, F[ExitCode]] =
    HttpServer.make[F](env.config, _ => allRoutes[F](env)).map { _ =>
      logger.info(s"OnlineShop http server is started").as(ExitCode.Success)
    }
}
