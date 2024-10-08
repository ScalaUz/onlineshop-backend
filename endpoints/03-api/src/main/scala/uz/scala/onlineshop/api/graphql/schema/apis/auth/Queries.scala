package uz.scala.onlineshop.api.graphql.schema.apis.auth

import caliban.schema.Annotations.GQLDescription
import uz.scala.onlineshop.Phone
import uz.scala.onlineshop.domain.auth.AuthTokens
import uz.scala.onlineshop.domain.auth.CustomerCredentials
import uz.scala.onlineshop.domain.auth.UserCredentials
import uz.scala.onlineshop.domain.customers.CustomerInput

case class Queries[F[_]](
    @GQLDescription("Login via OTP")
    loginViaOtp: CustomerCredentials => F[AuthTokens],
    @GQLDescription("Login by password")
    loginByPassword: UserCredentials => F[AuthTokens],
    @GQLDescription("Signup by password")
    signup: CustomerInput => F[AuthTokens],
    @GQLDescription("Send OTP")
    sendOtp: Phone => F[Unit],
    @GQLDescription("Refresh session")
    refresh: F[AuthTokens],
    @GQLDescription("Logout")
    logout: F[Unit],
  )
