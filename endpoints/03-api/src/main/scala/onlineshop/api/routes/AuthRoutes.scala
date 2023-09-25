package onlineshop.api.routes

import cats.Monad
import cats.MonadThrow
import cats.implicits.catsSyntaxApplyOps
import cats.implicits.toFlatMapOps
import org.http4s._
import org.http4s.circe.JsonDecoder
import org.typelevel.log4cats.Logger
import uz.scala.http4s.syntax.all.deriveEntityEncoder
import uz.scala.http4s.syntax.all.http4SyntaxReqOps
import uz.scala.http4s.utils.Routes

import onlineshop.auth.impl.Auth
import onlineshop.domain.AuthedUser
import onlineshop.domain.auth.Credentials

final case class AuthRoutes[F[_]: Monad: JsonDecoder: MonadThrow](
    auth: Auth[F, Option[AuthedUser]]
  )(implicit
    logger: Logger[F]
  ) extends Routes[F, Option[AuthedUser]] {
  override val path = "/auth"

  override val public: HttpRoutes[F] =
    HttpRoutes.of[F] {
      case req @ POST -> Root / "login" =>
        req.decodeR[Credentials] { credentials =>
          auth.login(credentials).flatMap(Ok(_))
        }
      case req @ GET -> Root / "refresh" =>
        auth.refresh(req).flatMap(Ok(_))
    }

  override val `private`: AuthedRoutes[Option[AuthedUser], F] = AuthedRoutes.of {
    case ar @ GET -> Root / "logout" as maybeUser =>
      maybeUser.fold(Forbidden("Your session expired")) { user =>
        auth.destroySession(ar.req, user.phone) *> NoContent()
      }
  }
}
