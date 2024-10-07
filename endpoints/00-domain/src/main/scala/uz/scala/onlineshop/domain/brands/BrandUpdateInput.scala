package uz.scala.onlineshop.domain.brands

import eu.timepit.refined.types.string.NonEmptyString
import uz.scala.onlineshop.domain.AssetId
import uz.scala.onlineshop.domain.BrandId

case class BrandUpdateInput(
    id: BrandId,
    name: Option[NonEmptyString],
    assetId: Option[AssetId],
  )
