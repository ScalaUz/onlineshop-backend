package uz.scala.onlineshop.repos.sql

import skunk._
import skunk.implicits._
import uz.scala.onlineshop.domain.ProductId
import uz.scala.onlineshop.repos.dto.Product

private[repos] object ProductsSql extends Sql[ProductId] {
  private val codec: Codec[Product] =
    (id *: nes *: nes *: nes *: nes *: nes *: nes *: nes *: price *: nni *: BrandsSql.id *: CategoriesSql.id *: zdt *: zdt.opt *: zdt.opt)
      .to[Product]
  val insert: Command[Product] =
    sql"""INSERT INTO products VALUES ($codec)""".command

  val fetch: Query[Void, Product] =
    sql"""SELECT * FROM products WHERE deleted_at IS NULL""".query(codec)

  val fetchById: Query[ProductId, Product] =
    sql"""SELECT * FROM products WHERE id = $id AND deleted_at IS NULL""".query(codec)

  val update: Command[Product] =
    sql"""UPDATE products
          SET name_uz = $nes,
            name_ru = $nes,
            name_en = $nes,
            description_uz = $nes,
            description_ru = $nes,
            description_en = $nes,
            link_code = $nes,
            price = $price,
            stock = $nni,
            brand_id = ${BrandsSql.id},
            category_id = ${CategoriesSql.id},
            updated_at = now()
          WHERE id = $id"""
      .command
      .contramap {
        case product: Product =>
          product.nameUz *: product.nameRu *: product.nameEn *: product.descriptionUz *: product.descriptionRu *:
            product.descriptionEn *: product.linkCode *: product.price *: product.stock *: product.brandId *:
            product.categoryId *: product.id *: EmptyTuple

      }

  val delete: Command[ProductId] =
    sql"""UPDATE products SET deleted_at = now() WHERE id = $id""".command
}
