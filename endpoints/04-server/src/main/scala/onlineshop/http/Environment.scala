package onlineshop.http

import cats.effect.Async
import org.http4s.server
import uz.scala.http4s.HttpServerConfig

import onlineshop.Algebras
import onlineshop.domain.AuthedUser
case class Environment[F[_]: Async](
    config: HttpServerConfig,
    middleware: server.AuthMiddleware[F, Option[AuthedUser]],
    algebras: Algebras[F],
  )
