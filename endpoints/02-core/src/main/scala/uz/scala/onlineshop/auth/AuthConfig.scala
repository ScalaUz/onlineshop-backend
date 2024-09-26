package uz.scala.onlineshop.auth

import uz.scala.onlineshop.domain.JwtAccessTokenKey
import uz.scala.onlineshop.domain.TokenExpiration

case class AuthConfig(
    tokenKey: JwtAccessTokenKey,
    accessTokenExpiration: TokenExpiration,
    refreshTokenExpiration: TokenExpiration,
    otpAttemptExpiration: TokenExpiration,
    otpExpiration: TokenExpiration,
    otpAttemptsLimit: Int,
  )
