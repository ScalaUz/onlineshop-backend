package onlineshop.domain.args

import eu.timepit.refined.types.string.NonEmptyString
import onlineshop.domain.AssetId

case class BrandInput(
    name: NonEmptyString,
    assetId: AssetId,
  )
