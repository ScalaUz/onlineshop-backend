package uz.scala.onlineshop.http

import cats.effect.Async
import org.http4s.server
import uz.scala.http4s.HttpServerConfig
import uz.scala.onlineshop.Algebras
import uz.scala.onlineshop.domain.AuthedUser
case class Environment[F[_]: Async](
    config: HttpServerConfig,
    middleware: server.AuthMiddleware[F, Option[AuthedUser]],
    algebras: Algebras[F],
  )
