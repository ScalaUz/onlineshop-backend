package uz.scala.onlineshop.repos.dto

import java.time.ZonedDateTime

import eu.timepit.refined.types.all.NonNegInt
import eu.timepit.refined.types.string.NonEmptyString
import io.scalaland.chimney.dsl.TransformationOps
import squants.Money
import uz.scala.onlineshop.Language
import uz.scala.onlineshop.domain
import uz.scala.onlineshop.domain.BrandId
import uz.scala.onlineshop.domain.CategoryId
import uz.scala.onlineshop.domain.ProductId

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
  ) {
  private def name(implicit lang: Language): NonEmptyString = lang match {
    case Language.En => descriptionEn
    case Language.Ru => descriptionRu
    case Language.Uz => descriptionUz
  }

  private def description(implicit lang: Language): NonEmptyString = lang match {
    case Language.En => descriptionEn
    case Language.Ru => descriptionRu
    case Language.Uz => descriptionUz
  }

  def toView(implicit lang: Language): domain.products.Product =
    this
      .into[domain.products.Product]
      .withFieldConst(_.name, name)
      .withFieldConst(_.description, description)
      .transform
}
