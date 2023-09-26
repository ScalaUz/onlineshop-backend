package onlineshop.api.graphql

import caliban.CalibanError.ExecutionError
import eu.timepit.refined.types.string.NonEmptyString
import uz.scala.syntax.refined.commonSyntaxAutoRefineV

import onlineshop.Phone
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

  implicit val nesArgBuilder: ArgBuilder[NonEmptyString] =
    ArgBuilder.string.map[NonEmptyString](identity(_))

  implicit val phoneArgBuilder: ArgBuilder[Phone] =
    ArgBuilder.string.map[Phone](identity(_))
  implicit val roleArgBuilder: ArgBuilder[Role] =
    ArgBuilder
      .string
      .flatMap[Role] { s =>
        Role.values.find(_.entryName == s).toRight(ExecutionError("Incorrect role entered"))
      }

  implicit val productArgsBuilder: ArgBuilder[ProductArgs] = ArgBuilder.gen
  implicit val userInfoArgsBuilder: ArgBuilder[UserInfo] = ArgBuilder.gen
  implicit val customerInfoArgsBuilder: ArgBuilder[CustomerInfo] = ArgBuilder.gen

  implicit val productSchema: Schema[GraphQLContext, Product] = Schema.gen[GraphQLContext, Product]
}
