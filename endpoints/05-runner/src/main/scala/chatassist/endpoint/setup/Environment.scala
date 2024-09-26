package chatassist.endpoint.setup

import cats.effect.Async
import cats.effect.Resource
import cats.effect.std.Console
import cats.effect.std.Dispatcher
import cats.effect.std.Random
import dev.profunktor.redis4cats.Redis
import dev.profunktor.redis4cats.effect.Log.NoOp.instance
import eu.timepit.refined.pureconfig._
import izumi.reflect.TagK
import org.http4s.server
import org.typelevel.log4cats.Logger
import pureconfig.generic.auto.exportReader
import uz.scala.aws.s3.S3Client
import uz.scala.database.Migrations
import uz.scala.onlineshop.Algebras
import uz.scala.onlineshop.Repositories
import uz.scala.onlineshop.auth.impl.LiveMiddleware
import uz.scala.onlineshop.domain.AuthedUser
import uz.scala.onlineshop.http.{ Environment => ServerEnvironment }
import uz.scala.onlineshop.utils.ConfigLoader
import uz.scala.redis.RedisClient
import uz.scala.skunk.SkunkSession

case class Environment[F[_]: Async: Logger: Dispatcher: Random](
    config: Config,
    repositories: Repositories[F],
    middleware: server.AuthMiddleware[F, Option[AuthedUser]],
    s3Client: S3Client[F],
    redis: RedisClient[F],
  ) {
  private val algebras: Algebras[F] = Algebras.make[F](config.auth, repositories, s3Client, redis)
  lazy val toServer: ServerEnvironment[F] =
    ServerEnvironment(
      config = config.http,
      middleware = middleware,
      algebras = algebras,
    )
}
object Environment {
  def make[F[_]: TagK: Async: Console: Logger: Dispatcher]: Resource[F, Environment[F]] =
    for {
      config <- Resource.eval(ConfigLoader.load[F, Config])
      _ <- Resource.eval(Migrations.run[F](config.migrations))
      repositories <- SkunkSession.make[F](config.database).map { implicit session =>
        Repositories.make[F]
      }
      redis <- Redis[F].utf8(config.redis.uri.toString).map(RedisClient[F](_, config.redis.prefix))
      implicit0(random: Random[F]) <- Resource.eval(Random.scalaUtilRandom[F])

      middleware = LiveMiddleware.make[F](config.auth, redis)
      s3Client <- S3Client.resource(config.awsConfig)
    } yield Environment[F](config, repositories, middleware, s3Client, redis)
}
