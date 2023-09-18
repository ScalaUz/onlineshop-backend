package onlineshop.domain

import io.circe.generic.JsonCodec

@JsonCodec
case class Category(name: String)
