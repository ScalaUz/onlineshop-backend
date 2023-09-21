package onlineshop.domain

import java.util.UUID

import io.circe.generic.JsonCodec

@JsonCodec
case class AuthInfo(userId: UUID)
