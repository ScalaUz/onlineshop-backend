package onlineshop.domain

import java.time.ZonedDateTime

import eu.timepit.refined.types.string.NonEmptyString

case class Asset(
    id: AssetId,
    createdAt: ZonedDateTime,
    s3Bucket: NonEmptyString,
    s3Key: NonEmptyString,
    public: Boolean,
    fileName: Option[NonEmptyString],
  )
