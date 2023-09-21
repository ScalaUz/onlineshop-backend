package chatassist.endpoint.setup

import caliban.interop.cats.CatsInterop
import cats.effect.Async
import cats.effect.Resource
import cats.effect.std.Console
import cats.effect.std.Dispatcher
import chatassist.support.database.Migrations
import eu.timepit.refined.pureconfig._
import izumi.reflect.TagK
import org.typelevel.log4cats.Logger
import pureconfig.generic.auto.exportReader
import uz.scala.skunk.SkunkSession
import zio.Runtime
import zio.ZEnvironment
import onlineshop.Algebras
import onlineshop.Repositories
import onlineshop.algebras.Products
import onlineshop.api.graphql.GraphQLContext
import onlineshop.http.{Environment => ServerEnvironment}
import onlineshop.utils.ConfigLoader
case class Environment[F[_]: TagK: Async: Logger: Dispatcher](
    config: Config,
    repositories: Repositories[F],
  )(implicit
    dispatcher: Dispatcher[F]
  ) {
  val Repositories(productsRepository, categoriesRepository) = repositories
  val products: Products[F] = Products.make[F](productsRepository)
  val algebras: Algebras[F] = Algebras[F](products)

  private lazy val graphQLContext: GraphQLContext[F] = GraphQLContext[F](algebras = algebras)
  implicit val runtime: Runtime[GraphQLContext[F]] =
    Runtime.default.withEnvironment(ZEnvironment(graphQLContext))
  implicit val interop: CatsInterop[F, GraphQLContext[F]] =
    CatsInterop.default[F, GraphQLContext[F]](dispatcher)
  val graphQL = new GraphQLEndpoints[F](algebras)
  lazy val toServer: ServerEnvironment[F] =
    ServerEnvironment(
      config = config.http,
      graphQL = graphQL.createGraphQL,
    )
}
object Environment {
  def make[F[_]: TagK: Async: Console: Logger]: Resource[F, Environment[F]] =
    for {
      config <- Resource.eval(ConfigLoader.load[F, Config])
      _ <- Resource.eval(Migrations.run[F](config.migrations))
      repositories <- SkunkSession.make[F](config.database).map { implicit session =>
        Repositories.make[F]
      }
      implicit0(dispatcher: Dispatcher[F]) <- Dispatcher.parallel[F]

    } yield Environment[F](config, repositories)
}
