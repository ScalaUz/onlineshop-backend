package chatassist.endpoint

import cats.effect.ExitCode
import cats.effect.IO
import cats.effect.IOApp
import cats.effect.Resource
import cats.effect.std.Dispatcher
import chatassist.endpoint.setup.Environment
import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.slf4j.Slf4jLogger
import uz.scala.onlineshop.HttpModule

object Main extends IOApp {
  implicit val logger: Logger[IO] = Slf4jLogger.getLogger[IO]
  override protected def computeWorkerThreadCount: Int =
    sys
      .env
      .get("CE_COMPUTE_WORKER_COUNT")
      .flatMap(_.toIntOption)
      .getOrElse(super.computeWorkerThreadCount)

  private def runnable: Resource[IO, ExitCode] =
    for {
      implicit0(dispatcher: Dispatcher[IO]) <- Dispatcher.parallel[IO]
      env <- Environment.make[IO]

      _ <- HttpModule.make[IO](env.toServer)

    } yield ExitCode.Success

  override def run(
      args: List[String]
    ): IO[ExitCode] =
    runnable.useForever
}
