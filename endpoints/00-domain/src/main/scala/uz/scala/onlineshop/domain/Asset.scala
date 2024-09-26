package uz.scala.onlineshop.domain

import java.time.ZonedDateTime

import eu.timepit.refined.types.string.NonEmptyString

case class Asset(
    id: AssetId,
    createdAt: ZonedDateTime,
    s3Key: NonEmptyString,
    fileName: Option[NonEmptyString],
    mediaType: Option[NonEmptyString],
  )
object Asset {
  case class AssetInfo(
      fileName: Option[NonEmptyString],
      mediaType: Option[NonEmptyString],
    )
}
