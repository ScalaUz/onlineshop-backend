package uz.scala.onlineshop.domain.brands

import eu.timepit.refined.types.string.NonEmptyString
import uz.scala.onlineshop.domain.{AssetId, BrandId}

case class Brand(
    id: BrandId,
    name: NonEmptyString,
    assetId: AssetId,
  )
