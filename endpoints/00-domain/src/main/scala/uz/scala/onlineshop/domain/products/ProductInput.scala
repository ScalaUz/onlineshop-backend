package uz.scala.onlineshop.domain.products

import eu.timepit.refined.types.all.NonNegInt
import eu.timepit.refined.types.string.NonEmptyString
import squants.Money
import uz.scala.onlineshop.domain.BrandId
import uz.scala.onlineshop.domain.CategoryId

case class ProductInput(
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
  )
