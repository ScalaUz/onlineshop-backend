package onlineshop.repos.dto

import java.time.ZonedDateTime

import eu.timepit.refined.types.string.NonEmptyString

import onlineshop.domain.AssetId
import onlineshop.domain.BrandId

case class Brand(
    id: BrandId,
    name: NonEmptyString,
    assetId: AssetId,
    updatedAt: Option[ZonedDateTime],
    deletedAt: Option[ZonedDateTime],
  )
