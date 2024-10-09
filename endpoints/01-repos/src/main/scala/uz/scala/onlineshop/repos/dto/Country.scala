package uz.scala.onlineshop.repos.dto

import eu.timepit.refined.types.string.NonEmptyString
import io.scalaland.chimney.dsl.TransformationOps
import uz.scala.onlineshop.Language
import uz.scala.onlineshop.domain
import uz.scala.onlineshop.domain.CountryId

case class Country(
    id: CountryId,
    nameUz: NonEmptyString,
    nameRu: NonEmptyString,
    nameEn: NonEmptyString,
  ) {
  private def name(implicit lang: Language): NonEmptyString =
    lang match {
      case Language.En => nameUz
      case Language.Ru => nameRu
      case Language.Uz => nameEn
    }

  def toDomain(implicit lang: Language): domain.Country =
    this
      .into[domain.Country]
      .withFieldConst(_.name, name)
      .transform
}
