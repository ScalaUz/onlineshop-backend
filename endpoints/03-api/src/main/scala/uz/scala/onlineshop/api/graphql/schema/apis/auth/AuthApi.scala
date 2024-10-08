package uz.scala.onlineshop.api.graphql.schema.apis.auth

import caliban.GraphQL
import caliban.RootResolver
import caliban.graphQL
import caliban.schema.Schema
import cats.Monad
import cats.effect.std.Dispatcher
import cats.implicits.toFoldableOps
import dev.profunktor.auth.jwt
import uz.scala.onlineshop.Language
import uz.scala.onlineshop.api.graphql.GraphQLContext
import uz.scala.onlineshop.api.graphql.GraphQLTypes
import uz.scala.onlineshop.api.graphql.schema.GraphqlApi
import uz.scala.onlineshop.auth.impl.Auth

class AuthApi[F[_]: Dispatcher, R](queries: Queries[F])
    extends GraphqlApi[F, R]
       with GraphQLTypes[GraphQLContext] {
  import Schema.auto._
  import caliban.interop.cats.implicits.catsEffectSchema
  override def graphql: GraphQL[R] =
    graphQL(RootResolver(queries))
}

object AuthApi {
  def make[F[_]: Monad: Dispatcher, R](
      auth: Auth[F, Option[uz.scala.onlineshop.domain.AuthedUser]]
    )(implicit
      lang: Language,
      jwtToken: Option[jwt.JwtToken],
      ctx: GraphQLContext,
    ): GraphqlApi[F, R] =
    new AuthApi[F, R](
      queries = Queries[F](
        loginViaOtp = auth.loginByOTP,
        loginByPassword = auth.loginByPassword,
        signup = auth.signup,
        sendOtp = auth.sendOtp,
        refresh = auth.refresh,
        logout = ctx.authInfo.traverse_(user => auth.destroySession(user.login)),
      )
    )
}
