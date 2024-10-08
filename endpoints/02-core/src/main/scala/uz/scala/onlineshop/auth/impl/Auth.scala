package uz.scala.onlineshop.auth.impl

import scala.concurrent.duration.DurationInt

import cats.data.EitherT
import cats.data.OptionT
import cats.effect.Sync
import cats.effect.std.Random
import cats.implicits._
import dev.profunktor.auth.jwt
import dev.profunktor.auth.jwt.JwtAuth
import dev.profunktor.auth.jwt.JwtSymmetricAuth
import dev.profunktor.auth.jwt.JwtToken
import org.typelevel.log4cats.Logger
import pdi.jwt.JwtAlgorithm
import tsec.passwordhashers.jca.SCrypt
import uz.scala.onlineshop.Language
import uz.scala.onlineshop.Phone
import uz.scala.onlineshop.ResponseMessages._
import uz.scala.onlineshop.algebras.CustomersAlgebra
import uz.scala.onlineshop.algebras.UsersAlgebra
import uz.scala.onlineshop.auth.AuthConfig
import uz.scala.onlineshop.auth.utils.AuthMiddleware
import uz.scala.onlineshop.auth.utils.AuthMiddleware.OTP_ATTEMPT_PREFIX
import uz.scala.onlineshop.auth.utils.AuthMiddleware.OTP_PREFIX
import uz.scala.onlineshop.auth.utils.JwtExpire
import uz.scala.onlineshop.auth.utils.Tokens
import uz.scala.onlineshop.domain.AuthedUser
import uz.scala.onlineshop.domain.auth._
import uz.scala.onlineshop.domain.customers.CustomerInput
import uz.scala.onlineshop.exception.AError
import uz.scala.onlineshop.exception.AError.AuthError
import uz.scala.onlineshop.exception.AError.AuthError.NoSuchUser
import uz.scala.onlineshop.exception.AError.AuthError.PasswordDoesNotMatch
import uz.scala.redis.RedisClient
import uz.scala.syntax.all.circeSyntaxDecoderOps
import uz.scala.syntax.refined.commonSyntaxAutoUnwrapV

trait Auth[F[_], A] {
  def loginByOTP(
      credentials: CustomerCredentials
    )(implicit
      language: Language
    ): F[AuthTokens]
  def loginByPassword(
      credentials: UserCredentials
    )(implicit
      language: Language
    ): F[AuthTokens]
  def signup(userInput: CustomerInput)(implicit language: Language): F[AuthTokens]
  def sendOtp(phone: Phone)(implicit language: Language): F[Unit]
  def destroySession(login: String)(implicit jwtToken: Option[jwt.JwtToken]): F[Unit]
  def refresh(implicit language: Language, jwtToken: Option[jwt.JwtToken]): F[AuthTokens]
}

object Auth {
  def make[F[_]: Sync: Random](
      config: AuthConfig,
      users: UsersAlgebra[F],
      customers: CustomersAlgebra[F],
      redis: RedisClient[F],
    )(implicit
      logger: Logger[F]
    ): Auth[F, Option[AuthedUser]] =
    new Auth[F, Option[AuthedUser]] {
      val tokens: Tokens[F] =
        Tokens.make[F](JwtExpire[F], config)
      val jwtAuth: JwtSymmetricAuth = JwtAuth.hmac(config.tokenKey.secret, JwtAlgorithm.HS256)

      override def loginByOTP(
          credentials: CustomerCredentials
        )(implicit
          language: Language
        ): F[AuthTokens] =
        for {
          _ <- OptionT(redis.get(s"$OTP_PREFIX:${credentials.phone}"))
            .filter(_ == credentials.password.value)
            .getOrRaise(PasswordDoesNotMatch(PASSWORD_DOES_NOT_MATCH(language)))

          customer <- OptionT(customers.findByPhone(credentials.phone))
            .getOrRaise(NoSuchUser(USER_NOT_FOUND(language)))

          authTokens <- processCreateToken(customer.toDomain)
        } yield authTokens

      override def loginByPassword(
          credentials: UserCredentials
        )(implicit
          language: Language
        ): F[AuthTokens] =
        users.findByEmail(credentials.email).flatMap {
          case None =>
            NoSuchUser(USER_NOT_FOUND(language)).raiseError[F, AuthTokens]
          case Some(user) if !SCrypt.checkpwUnsafe(credentials.password.value, user.password) =>
            PasswordDoesNotMatch(PASSWORD_DOES_NOT_MATCH(language)).raiseError[F, AuthTokens]
          case Some(user) => processCreateToken(user.toDomain)
        }

      override def signup(
          customerInput: CustomerInput
        )(implicit
          language: Language
        ): F[AuthTokens] =
        for {
          _ <- OptionT(redis.get(s"$OTP_PREFIX:${customerInput.phone}"))
            .filter(_ == customerInput.password.value)
            .getOrRaise(PasswordDoesNotMatch(PASSWORD_DOES_NOT_MATCH(language)))

          user <- customers.create(customerInput)
          authTokens <- processCreateToken(user.toDomain)
        } yield authTokens

      override def sendOtp(phone: Phone)(implicit language: Language): F[Unit] =
        processOTP(phone)(
          for {
            otp <- Random[F].betweenInt(1000, 9999).map(_.toString)
//            _ <- messages.sendSms(otp, phone)
            _ <- logger.info(s"OTP sent to $phone, otp: $otp")
            _ <- redis.put(s"$OTP_PREFIX:$phone", otp, config.otpExpiration.value)
          } yield {}
        )

      override def refresh(
          implicit
          language: Language,
          jwtToken: Option[jwt.JwtToken],
        ): F[AuthTokens] =
        for {
          refreshToken <- EitherT(
            AuthMiddleware
              .getAndValidateJwtToken[F](
                jwtAuth,
                token =>
                  for {
                    _ <- OptionT(redis.get(AuthMiddleware.REFRESH_TOKEN_PREFIX + token))
                      .semiflatMap(_.decodeAsF[F, AuthedUser])
                      .semiflatMap(user => redis.del(user.login))
                      .value
                    _ <- redis.del(AuthMiddleware.REFRESH_TOKEN_PREFIX + token.value)
                  } yield {},
              )
              .apply(jwtToken)
          ).leftMap(AuthError.InvalidToken.apply).rethrowT

          tokens <- OptionT(redis.get(refreshToken.value))
            .semiflatMap(_.decodeAsF[F, AuthTokens])
            .getOrElseF(refreshTokens(refreshToken))

        } yield tokens

      override def destroySession(login: String)(implicit jwtToken: Option[jwt.JwtToken]): F[Unit] =
        jwtToken
          .traverse_(token => redis.del(AuthMiddleware.ACCESS_TOKEN_PREFIX + token.value, login))

      private def processCreateToken(user: AuthedUser)(implicit language: Language): F[AuthTokens] =
        OptionT(redis.get(user.login))
          .cataF(
            createNewToken(user),
            json =>
              for {
                tokens <- json.decodeAsF[F, AuthTokens]
                validTokens <- EitherT(
                  AuthMiddleware
                    .validateJwtToken[F](
                      JwtToken(tokens.accessToken),
                      jwtAuth,
                      _ => redis.del(tokens.accessToken, tokens.refreshToken, user.login),
                    )
                ).foldF(
                  error =>
                    logger.info(s"Tokens recreated reason of that: $error") *>
                      createNewToken(user),
                  _ => tokens.pure[F],
                )
              } yield validTokens,
          )

      private def refreshTokens(
          refreshToken: JwtToken
        )(implicit
          language: Language
        ): F[AuthTokens] =
        (for {
          user <- EitherT
            .fromOptionF(
              redis.get(AuthMiddleware.REFRESH_TOKEN_PREFIX + refreshToken.value),
              AuthError.InvalidToken(INVALID_TOKEN(language)),
            )
            .semiflatMap(_.decodeAsF[F, AuthedUser])
          _ <- EitherT.right[AuthError](clearOldTokens(user.login))
          tokens <- EitherT.right[AuthError](createNewToken(user))
          _ <- EitherT.right[AuthError](redis.put(refreshToken.value, tokens, 1.minute))
        } yield tokens).rethrowT

      private def createNewToken(user: AuthedUser): F[AuthTokens] =
        for {
          tokens <- tokens.createToken[AuthedUser](user)
          accessToken = AuthMiddleware.ACCESS_TOKEN_PREFIX + tokens.accessToken
          refreshToken = AuthMiddleware.REFRESH_TOKEN_PREFIX + tokens.refreshToken
          _ <- redis.put(accessToken, user, config.accessTokenExpiration.value)
          _ <- redis.put(refreshToken, user, config.refreshTokenExpiration.value)
          _ <- redis.put(user.login, tokens, config.refreshTokenExpiration.value)
        } yield tokens

      private def clearOldTokens(login: String): F[Unit] =
        OptionT(redis.get(login))
          .semiflatMap(_.decodeAsF[F, AuthTokens])
          .semiflatMap(tokens =>
            redis.del(
              s"${AuthMiddleware.REFRESH_TOKEN_PREFIX}${tokens.refreshToken}",
              s"${AuthMiddleware.ACCESS_TOKEN_PREFIX}${tokens.accessToken}",
            )
          )
          .value
          .void

      private def processOTP(phone: Phone)(fa: F[Unit])(implicit language: Language): F[Unit] =
        OptionT(redis.get(s"$OTP_ATTEMPT_PREFIX:$phone"))
          .semiflatMap(_.decodeAsF[F, Int])
          .getOrElse(0)
          .flatMap { attempt =>
            if (attempt < config.otpAttemptsLimit)
              fa.flatTap(_ =>
                redis.put(
                  s"$OTP_ATTEMPT_PREFIX:$phone",
                  attempt + 1,
                  config.otpAttemptExpiration.value,
                )
              )
            else
              AError.AuthError.LimitExceeded(LIMIT_EXCEEDED(language)).raiseError[F, Unit]
          }
    }
}
