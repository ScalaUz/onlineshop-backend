package onlineshop.domain

import io.circe.generic.JsonCodec

@JsonCodec
case class Product(name: String)