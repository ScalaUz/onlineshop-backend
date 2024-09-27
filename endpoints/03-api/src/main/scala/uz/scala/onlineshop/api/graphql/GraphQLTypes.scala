package uz.scala.onlineshop.api.graphql

import caliban.schema.GenericSchema
import caliban.schema._
import eu.timepit.refined.types.string.NonEmptyString
import squants.Money
import uz.scala.domain.UZS
import uz.scala.onlineshop.Phone
import uz.scala.onlineshop.domain._
import uz.scala.onlineshop.domain.brands.Brand
import uz.scala.onlineshop.domain.brands.BrandInput
import uz.scala.onlineshop.domain.categories.Category
import uz.scala.onlineshop.domain.categories.CategoryInput
import uz.scala.onlineshop.domain.enums.Role
import uz.scala.onlineshop.domain.products.Product
import uz.scala.onlineshop.effects.IsUUID
import uz.scala.syntax.refined._

trait GraphQLTypes[R] extends GenericSchema[R] {
  private def idArgBuilder[ID](implicit uuid: IsUUID[ID]): ArgBuilder[ID] =
    ArgBuilder.uuid.map(uuid.uuid.get)

  implicit val roleArgBuilder: ArgBuilder[Role] = ArgBuilder.gen
  implicit val nonEmptyStringArgBuilder: ArgBuilder[NonEmptyString] =
    ArgBuilder.string.map(identity(_))
  implicit val phoneArgBuilder: ArgBuilder[Phone] =
    ArgBuilder.string.map(identity(_))
  implicit val MoneyBuilder: ArgBuilder[Money] =
    ArgBuilder.bigDecimal.map(UZS(_))
  implicit val assetIdArgBuilder: ArgBuilder[AssetId] = idArgBuilder[AssetId]
  implicit val ProductIdArgBuilder: ArgBuilder[ProductId] = idArgBuilder[ProductId]
  implicit val categoryInputArgsBuilder: ArgBuilder[CategoryInput] = ArgBuilder.gen
  implicit val brandInputArgsBuilder: ArgBuilder[BrandInput] = ArgBuilder.gen

  // Schemas
  private def idSchema[ID](implicit uuid: IsUUID[ID]): Schema.Typeclass[ID] =
    Schema.uuidSchema.contramap(uuid.uuid.apply)
  implicit val productIdSchema: Schema.Typeclass[ProductId] = idSchema[ProductId]
  implicit val personIdSchema: Schema.Typeclass[UserId] = idSchema[UserId]
  implicit val brandIdSchema: Schema.Typeclass[BrandId] = idSchema[BrandId]
  implicit val categoryIdSchema: Schema.Typeclass[CategoryId] = idSchema[CategoryId]
  implicit val countryIdSchema: Schema.Typeclass[CountryId] = idSchema[CountryId]
  implicit val regionIdSchema: Schema.Typeclass[RegionId] = idSchema[RegionId]
  implicit val cityIdSchema: Schema.Typeclass[CityId] = idSchema[CityId]
  implicit val assetIdSchema: Schema.Typeclass[AssetId] = idSchema[AssetId]
  implicit val nonEmptyStringSchema: Schema.Typeclass[NonEmptyString] =
    Schema.stringSchema.contramap[NonEmptyString](identity(_))
  implicit val moneySchema: Schema.Typeclass[Money] =
    Schema.bigDecimalSchema.contramap[Money](_.amount)

  implicit val productSchema: Schema.Typeclass[Product] = Schema.gen
  implicit val categorySchema: Schema.Typeclass[Category] = Schema.gen
  implicit val BrandSchema: Schema.Typeclass[Brand] = Schema.gen
}
