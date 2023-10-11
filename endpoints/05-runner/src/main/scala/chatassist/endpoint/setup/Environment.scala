package chatassist.endpoint.setup

import cats.Monad
import cats.data.OptionT
import cats.effect.Async
import cats.effect.Resource
import cats.effect.std.Console
import cats.effect.std.Dispatcher
import dev.profunktor.redis4cats.Redis
import dev.profunktor.redis4cats.effect.Log.NoOp.instance
import eu.timepit.refined.pureconfig._
import izumi.reflect.TagK
import org.http4s.server
import org.typelevel.log4cats.Logger
import pureconfig.generic.auto.exportReader
import uz.scala.database.Migrations
import uz.scala.redis.RedisClient
import uz.scala.skunk.SkunkSession

import onlineshop.Algebras
import onlineshop.Phone
import onlineshop.Repositories
import onlineshop.algebras.Brands
import onlineshop.algebras.Categories
import onlineshop.algebras.Customers
import onlineshop.algebras.Products
import onlineshop.algebras.Users
import onlineshop.api.graphql.GraphQLEndpoints
import onlineshop.auth.impl.Auth
import onlineshop.auth.impl.LiveMiddleware
import onlineshop.domain.AccessCredentials
import onlineshop.domain.AuthedUser
import onlineshop.http.{ Environment => ServerEnvironment }
import onlineshop.utils.ConfigLoader

case class Environment[F[_]: TagK: Async: Logger: Dispatcher](
    config: Config,
    repositories: Repositories[F],
    auth: Auth[F, Option[AuthedUser]],
    middleware: server.AuthMiddleware[F, Option[AuthedUser]],
  ) {
  private val Repositories(
    productsRepository,
    categoriesRepository,
    brandsRepository,
    customersRepository,
    usersRepository,
  ) = repositories
  private val brands: Brands[F] = Brands.make[F](brandsRepository)
  private val products: Products[F] = Products.make[F](productsRepository)
  private val categories: Categories[F] = Categories.make[F](categoriesRepository)
  private val customers: Customers[F] = Customers.make[F](customersRepository)
  private val users: Users[F] = Users.make[F](usersRepository)
  private val algebras: Algebras[F] = Algebras[F](brands, products, categories, customers, users)

  lazy val toServer: ServerEnvironment[F] =
    ServerEnvironment(
      config = config.http,
      graphQL = maybeUser => new GraphQLEndpoints[F](algebras, maybeUser),
      middleware = middleware,
      auth = auth,
    )
}
object Environment {
  private def findUser[F[_]: Monad](
      repositories: Repositories[F]
    ): Phone => F[Option[AccessCredentials[AuthedUser]]] = phone =>
    OptionT(repositories.users.find(phone))
      .map(identity[AccessCredentials[AuthedUser]])
      .orElse(
        OptionT(repositories.customers.find(phone)).map(identity[AccessCredentials[AuthedUser]])
      )
      .value

  def make[F[_]: TagK: Async: Console: Logger]: Resource[F, Environment[F]] =
    for {
      config <- Resource.eval(ConfigLoader.load[F, Config])
      _ <- Resource.eval(Migrations.run[F](config.migrations))
      repositories <- SkunkSession.make[F](config.database).map { implicit session =>
        Repositories.make[F]
      }
      redis <- Redis[F].utf8(config.redis.uri.toString).map(RedisClient[F](_, config.redis.prefix))

      implicit0(dispatcher: Dispatcher[F]) <- Dispatcher.parallel[F]
      middleware = LiveMiddleware.make[F](config.auth, redis)
      auth = Auth.make[F](config.auth, findUser(repositories), redis)
    } yield Environment[F](config, repositories, auth, middleware)
}
