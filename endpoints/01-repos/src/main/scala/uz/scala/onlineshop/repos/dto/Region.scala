package uz.scala.onlineshop.repos.dto

import eu.timepit.refined.types.string.NonEmptyString
import io.scalaland.chimney.dsl.TransformationOps
import uz.scala.onlineshop.Language
import uz.scala.onlineshop.domain
import uz.scala.onlineshop.domain.CountryId
import uz.scala.onlineshop.domain.RegionId

case class Region(
    id: RegionId,
    countryId: CountryId,
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

  def toDomain(implicit lang: Language): domain.Region =
    this
      .into[domain.Region]
      .withFieldConst(_.name, name)
      .transform
}
