package onlineshop.repos.dto

import java.time.ZonedDateTime

import eu.timepit.refined.types.string.NonEmptyString

import onlineshop.domain.CategoryId

case class Category(
    id: CategoryId,
    nameUz: NonEmptyString,
    nameRu: NonEmptyString,
    nameEn: NonEmptyString,
    parentId: Option[CategoryId],
    updatedAt: Option[ZonedDateTime],
    deletedAt: Option[ZonedDateTime],
  )
