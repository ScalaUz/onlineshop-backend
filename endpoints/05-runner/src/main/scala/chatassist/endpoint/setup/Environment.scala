package chatassist.endpoint.setup

import cats.effect.Async
import cats.effect.Resource
import cats.effect.std.Console
import cats.effect.std.Dispatcher
import cats.implicits.catsSyntaxApplicativeId
import chatassist.support.database.Migrations
import eu.timepit.refined.pureconfig._
import org.typelevel.log4cats.Logger
import pureconfig.generic.auto.exportReader
import sangria.schema.Schema
import uz.scala.skunk.SkunkSession

import onlineshop.Repositories
import onlineshop.api.graphql.GraphQL
import onlineshop.http.{ Environment => ServerEnvironment }
import onlineshop.utils.ConfigLoader
case class Environment[F[_]: Async: Logger: Dispatcher](
    config: Config,
    repositories: Repositories[F],
  ) {
  private val graphQL: GraphQL[F] =
    GraphQL[F](
      Schema(
        query = new GraphQLEndpoints[F](this).queryType
      ),
      repositories.pure[F],
    )

  lazy val toServer: ServerEnvironment[F] =
    ServerEnvironment(
      config = config.http,
      graphQL = graphQL,
    )
}
object Environment {
  def make[F[_]: Console: Logger](implicit F: Async[F]): Resource[F, Environment[F]] =
    for {
      config <- Resource.eval(ConfigLoader.load[F, Config])
      _ <- Resource.eval(Migrations.run[F](config.migrations))

      repositories <- SkunkSession.make[F](config.database).map { implicit session =>
        Repositories.make[F]
      }
      implicit0(dispatcher: Dispatcher[F]) <- Dispatcher.parallel[F]
    } yield Environment[F](config, repositories)
}
