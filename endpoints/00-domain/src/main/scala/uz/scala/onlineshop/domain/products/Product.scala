package uz.scala.onlineshop.domain.products

import java.time.ZonedDateTime

import eu.timepit.refined.types.string.NonEmptyString
import squants.Money
import uz.scala.onlineshop.domain.BrandId
import uz.scala.onlineshop.domain.CategoryId
import uz.scala.onlineshop.domain.ProductId

case class Product(
    id: ProductId,
    createdAt: ZonedDateTime,
    name: NonEmptyString,
    linkCode: NonEmptyString,
    price: Money,
    description: NonEmptyString,
    brandId: BrandId,
    categoryId: CategoryId,
  )
