package uz.scala.onlineshop.domain.auth

import io.circe.generic.JsonCodec

@JsonCodec
case class AuthTokens(accessToken: String, refreshToken: String)
