package onlineshop.domain.args

import io.circe.generic.JsonCodec

@JsonCodec
case class ProductArgs(
    name: Option[String]
  )
