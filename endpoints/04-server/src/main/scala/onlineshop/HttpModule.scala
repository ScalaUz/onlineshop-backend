package onlineshop

import caliban.GraphQL
import caliban.interop.cats.CatsInterop
import cats.data.NonEmptyList
import cats.effect.Async
import cats.effect.ExitCode
import cats.effect.kernel.Resource
import cats.implicits.toFunctorOps
import org.http4s.HttpRoutes
import org.typelevel.log4cats.Logger
import uz.scala.http4s.HttpServer

import onlineshop.api.graphql.GraphQLContext
import onlineshop.api.routes.GraphQLRoutes
import onlineshop.http.Environment

object HttpModule {
  private def allRoutes[F[_]: Async: Logger](
      graphQL: GraphQL[GraphQLContext[F]]
    )(implicit
      interop: CatsInterop[F, GraphQLContext[F]]
    ): F[NonEmptyList[HttpRoutes[F]]] =
    NonEmptyList
      .of[F[HttpRoutes[F]]](
        new GraphQLRoutes[F](graphQL).routes
      )
      .traverse(identity)

  def make[F[_]: Async](
      env: Environment[F]
    )(implicit
      logger: Logger[F],
      interop: CatsInterop[F, GraphQLContext[F]],
    ): Resource[F, F[ExitCode]] =
    for {
      routes <- Resource.eval(allRoutes[F](env.graphQL))
      _ <- HttpServer.make[F](env.config, _ => routes)
      res = logger.info(s"OnlineShop http server is started").as(ExitCode.Success)
    } yield res
}
