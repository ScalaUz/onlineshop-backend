package uz.scala.onlineshop.repos.dto

import eu.timepit.refined.types.string.NonEmptyString
import io.scalaland.chimney.dsl.TransformationOps
import uz.scala.onlineshop.Language
import uz.scala.onlineshop.domain
import uz.scala.onlineshop.domain.CityId
import uz.scala.onlineshop.domain.RegionId

case class City(
    id: CityId,
    regionId: RegionId,
    nameUz: NonEmptyString,
    nameRu: NonEmptyString,
    nameEn: NonEmptyString,
  ) {
  private def name(implicit lang: Language): NonEmptyString =
    lang match {
      case Language.En => nameEn
      case Language.Ru => nameRu
      case Language.Uz => nameUz
    }

  def toDomain(implicit lang: Language): domain.City =
    this
      .into[domain.City]
      .withFieldConst(_.name, name)
      .transform
}
