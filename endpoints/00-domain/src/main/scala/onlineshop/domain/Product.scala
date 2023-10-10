package onlineshop.domain

import java.time.ZonedDateTime

import eu.timepit.refined.types.string.NonEmptyString
import io.circe.generic.JsonCodec
import io.circe.refined._
import squants.Money

@JsonCodec
case class Product(
    id: ProductId,
    createdAt: ZonedDateTime,
    name: NonEmptyString,
    linkCode: NonEmptyString,
    price: Money,
    descriptionUz: NonEmptyString,
    descriptionRu: NonEmptyString,
    descriptionEn: NonEmptyString,
    brandId: BrandId,
    categoryId: CategoryId,
  )
