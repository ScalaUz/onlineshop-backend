package uz.scala.onlineshop.domain.products

import eu.timepit.refined.types.all.NonNegInt
import eu.timepit.refined.types.string.NonEmptyString
import squants.Money
import uz.scala.onlineshop.domain.BrandId
import uz.scala.onlineshop.domain.CategoryId
import uz.scala.onlineshop.domain.ProductId

case class ProductUpdateInput(
    productId: ProductId,
    nameUz: Option[NonEmptyString],
    nameRu: Option[NonEmptyString],
    nameEn: Option[NonEmptyString],
    descriptionUz: Option[NonEmptyString],
    descriptionRu: Option[NonEmptyString],
    descriptionEn: Option[NonEmptyString],
    linkCode: Option[NonEmptyString],
    price: Option[Money],
    stock: Option[NonNegInt],
    brandId: Option[BrandId],
    categoryId: Option[CategoryId],
  )
