package onlineshop.api.graphql

import caliban.CalibanError.ExecutionError
import cats.Id
import cats.data.EitherT
import io.circe.Decoder
import io.circe.DecodingFailure
import io.circe.refined._
import io.circe.syntax.EncoderOps

import onlineshop.domain.PersonId
import onlineshop.domain.Product
import onlineshop.domain.args.CustomerInfo
import onlineshop.domain.args.ProductArgs
import onlineshop.domain.args.UserInfo
import onlineshop.domain.enums.Role

trait GraphQLTypes {
  import caliban.schema._

  implicit val personIdSchema: Schema[GraphQLContext, PersonId] =
    Schema.uuidSchema.contramap(_.value)

  implicit def argBuilder[A: Decoder]: ArgBuilder[A] =
    ArgBuilder
      .string
      .flatMap[A](a =>
        EitherT[Id, DecodingFailure, A](Decoder[A].decodeJson(a.asJson))
          .leftMap(r => ExecutionError(r.message))
          .value
      )
  implicit val roleArgBuilder: ArgBuilder[Role] = ArgBuilder.gen

  implicit val productArgsBuilder: ArgBuilder[ProductArgs] = ArgBuilder.gen
  implicit val userInfoArgsBuilder: ArgBuilder[UserInfo] = ArgBuilder.gen
  implicit val customerInfoArgsBuilder: ArgBuilder[CustomerInfo] = ArgBuilder.gen

  implicit val productSchema: Schema[GraphQLContext, Product] = Schema.gen[GraphQLContext, Product]
}
