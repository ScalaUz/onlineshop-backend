package onlineshop.repos.dto

import java.time.ZonedDateTime

import eu.timepit.refined.types.all.NonNegInt
import eu.timepit.refined.types.string.NonEmptyString
import squants.Money

import onlineshop.domain.BrandId
import onlineshop.domain.CategoryId
import onlineshop.domain.ProductId

case class Product(
    id: ProductId,
    nameUz: NonEmptyString,
    nameRu: NonEmptyString,
    nameEn: NonEmptyString,
    descriptionUz: NonEmptyString,
    descriptionRu: NonEmptyString,
    descriptionEn: NonEmptyString,
    linkCode: NonEmptyString,
    price: Money,
    stock: NonNegInt,
    brandId: BrandId,
    categoryId: CategoryId,
    createdAt: ZonedDateTime,
    updatedAt: Option[ZonedDateTime],
    deletedAt: Option[ZonedDateTime],
  )
