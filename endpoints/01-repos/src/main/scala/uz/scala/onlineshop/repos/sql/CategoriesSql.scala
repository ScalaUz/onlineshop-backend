package uz.scala.onlineshop.repos.sql

import skunk._
import skunk.implicits._
import uz.scala.onlineshop.domain.CategoryId
import uz.scala.onlineshop.repos.dto.Category

private[repos] object CategoriesSql extends Sql[CategoryId] {
  private val codec: Codec[Category] = (id *: nes *: nes *: nes *: id.opt *: zdt.opt *: zdt.opt).to[Category]
  val insert: Command[Category] =
    sql"""INSERT INTO categories VALUES ($codec)""".command

  val fetch: Query[Void, Category] =
    sql"""SELECT * FROM categories""".query(codec)

  val fetchById: Query[CategoryId, Category] =
    sql"""SELECT * FROM categories WHERE id = $id AND deleted_at IS NULL LIMIT 1""".query(codec)

  val update: Command[Category] =
    sql"""UPDATE
           categories
          SET name_uz = $nes,
              name_ru = $nes,
              name_en = $nes,
              parent_id = ${id.opt},
              updated_at = now()
          WHERE id = $id"""
      .command
      .contramap { category: Category =>
        category.nameUz *: category.nameRu *: category.nameEn *: category.parentId *: category.id *: EmptyTuple
      }

  val delete: Command[CategoryId] =
    sql"""UPDATE
           categories
          SET deleted_at = now()
          WHERE id = $id""".command
}
