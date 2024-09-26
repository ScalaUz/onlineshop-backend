package uz.scala.onlineshop.domain.brands

import eu.timepit.refined.types.string.NonEmptyString
import uz.scala.onlineshop.domain.AssetId

case class BrandInput(
    name: NonEmptyString,
    assetId: AssetId,
  )
