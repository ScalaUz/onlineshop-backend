package uz.scala.onlineshop.repos.dto

import java.time.ZonedDateTime
import eu.timepit.refined.types.string.NonEmptyString
import io.scalaland.chimney.dsl.TransformationOps
import uz.scala.onlineshop.domain
import uz.scala.onlineshop.domain.AssetId
import uz.scala.onlineshop.domain.BrandId

case class Brand(
    id: BrandId,
    name: NonEmptyString,
    assetId: AssetId,
    updatedAt: Option[ZonedDateTime] = None,
    deletedAt: Option[ZonedDateTime] = None,
  ) {
  def toView: domain.brands.Brand = this.into[domain.brands.Brand].transform
}
