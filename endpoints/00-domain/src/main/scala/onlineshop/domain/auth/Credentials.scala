package onlineshop.domain
package auth

import eu.timepit.refined.types.string.NonEmptyString
import io.circe.generic.JsonCodec
import io.circe.refined._

import onlineshop.Phone

@JsonCodec
case class Credentials(phone: Phone, password: NonEmptyString)
