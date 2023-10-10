package onlineshop.api.graphql

import caliban.schema.GenericSchema
import eu.timepit.refined.types.string.NonEmptyString
import squants.Money
import uz.scala.syntax.refined._

import onlineshop.Phone
import onlineshop.domain._
import onlineshop.domain.args.CustomerInfo
import onlineshop.domain.args.ProductArgs
import onlineshop.domain.args.UserInfo
import onlineshop.domain.enums.Role
import onlineshop.effects.IsUUID
trait GraphQLTypes extends GenericSchema[GraphQLContext] {
  import caliban.schema._

  private def idSchema[ID](implicit uuid: IsUUID[ID]): Schema[GraphQLContext, ID] =
    Schema.uuidSchema.contramap(uuid.uuid.apply)
  private def idArgBuilder[ID](implicit uuid: IsUUID[ID]): ArgBuilder[ID] =
    ArgBuilder.uuid.map(uuid.uuid.get)

  implicit val roleArgBuilder: ArgBuilder[Role] = ArgBuilder.gen
  implicit val nonEmptyStringArgBuilder: ArgBuilder[NonEmptyString] =
    ArgBuilder.string.map(identity(_))
  implicit val phoneArgBuilder: ArgBuilder[Phone] =
    ArgBuilder.string.map(identity(_))

  implicit val productArgsBuilder: ArgBuilder[ProductArgs] = ArgBuilder.gen
  implicit val userInfoArgsBuilder: ArgBuilder[UserInfo] = ArgBuilder.gen
  implicit val customerInfoArgsBuilder: ArgBuilder[CustomerInfo] = ArgBuilder.gen
  implicit val productIdSchema: Schema[GraphQLContext, ProductId] = idSchema[ProductId]
  implicit val personIdSchema: Schema[GraphQLContext, PersonId] = idSchema[PersonId]
  implicit val BrandIdSchema: Schema[GraphQLContext, BrandId] = idSchema[BrandId]
  implicit val CategoryIdSchema: Schema[GraphQLContext, CategoryId] = idSchema[CategoryId]
  implicit val CountryIdSchema: Schema[GraphQLContext, CountryId] = idSchema[CountryId]
  implicit val RegionIdSchema: Schema[GraphQLContext, RegionId] = idSchema[RegionId]
  implicit val CityIdSchema: Schema[GraphQLContext, CityId] = idSchema[CityId]
  implicit val NonEmptyStringSchema: Schema[GraphQLContext, NonEmptyString] =
    Schema.stringSchema.contramap[NonEmptyString](identity(_))
  implicit val moneySchema: Schema[GraphQLContext, Money] =
    Schema.bigDecimalSchema.contramap[Money](_.amount)
}
