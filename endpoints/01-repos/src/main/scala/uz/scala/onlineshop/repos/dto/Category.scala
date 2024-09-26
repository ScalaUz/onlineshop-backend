package uz.scala.onlineshop.repos.dto

import java.time.ZonedDateTime

import eu.timepit.refined.types.string.NonEmptyString
import io.scalaland.chimney.dsl.TransformationOps
import uz.scala.onlineshop.Language
import uz.scala.onlineshop.domain
import uz.scala.onlineshop.domain.CategoryId

case class Category(
    id: CategoryId,
    nameUz: NonEmptyString,
    nameRu: NonEmptyString,
    nameEn: NonEmptyString,
    parentId: Option[CategoryId] = None,
    updatedAt: Option[ZonedDateTime] = None,
    deletedAt: Option[ZonedDateTime] = None,
  ) {
  private def name(implicit lang: Language): NonEmptyString = lang match {
    case Language.En => nameEn
    case Language.Ru => nameRu
    case Language.Uz => nameUz
  }
  def toView(implicit lang: Language): domain.categories.Category =
    this
      .into[domain.categories.Category]
      .withFieldConst(_.name, name)
      .transform
}
