package onlineshop.domain

import eu.timepit.refined.types.string.NonEmptyString
import uz.scala.syntax.refined.commonSyntaxAutoRefineV

import onlineshop.Phone
import onlineshop.domain.args.CustomerInfo
import onlineshop.domain.args.ProductArgs

trait GraphQLTypes {
  import caliban.schema._
  implicit val productSchema: Schema[Any, Product] = Schema.gen[Any, Product]
  implicit val personIdSchema: Schema[Any, PersonId] = Schema.uuidSchema.contramap(_.value)
  implicit val argsBuilder: ArgBuilder[CustomerInfo] = ArgBuilder.gen
  implicit val productArgsBuilder: ArgBuilder[ProductArgs] = ArgBuilder.gen

  implicit val nesArgBuilder: ArgBuilder[NonEmptyString] =
    ArgBuilder.string.map[NonEmptyString](identity(_))
  implicit val phoneArgBuilder: ArgBuilder[Phone] =
    ArgBuilder.string.map[Phone](identity(_))
}
