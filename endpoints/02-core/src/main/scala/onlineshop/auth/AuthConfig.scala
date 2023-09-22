package onlineshop.auth

import onlineshop.domain.JwtAccessTokenKey
import onlineshop.domain.TokenExpiration

case class AuthConfig(
    tokenKey: JwtAccessTokenKey,
    accessTokenExpiration: TokenExpiration,
    refreshTokenExpiration: TokenExpiration,
  )
