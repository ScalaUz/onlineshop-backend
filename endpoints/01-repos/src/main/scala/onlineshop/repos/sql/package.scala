package onlineshop.repos

import java.time.ZonedDateTime

import eu.timepit.refined.types.string.NonEmptyString
import skunk.Codec
import skunk.codec.all._
import skunk.data.Type
import squants.Money
import squants.market.USD
import tsec.passwordhashers.PasswordHash
import tsec.passwordhashers.jca.SCrypt
import uz.scala.syntax.refined.commonSyntaxAutoRefineV

import onlineshop.Phone
import onlineshop.domain.enums.Role
import onlineshop.effects.IsUUID

package object sql {
  def identification[A: IsUUID]: Codec[A] = uuid.imap[A](IsUUID[A].uuid.get)(IsUUID[A].uuid.apply)

  val nes: Codec[NonEmptyString] = varchar.imap[NonEmptyString](identity(_))(_.value)
  val phone: Codec[Phone] = varchar.imap[Phone](identity(_))(_.value)
  val zonedDateTime: Codec[ZonedDateTime] = timestamptz.imap(_.toZonedDateTime)(_.toOffsetDateTime)
  val role: Codec[Role] = `enum`[Role](Role, Type("role"))
  val price: Codec[Money] = numeric.imap[Money](money => USD(money))(_.amount)
  val passwordHash: Codec[PasswordHash[SCrypt]] =
    varchar.imap[PasswordHash[SCrypt]](PasswordHash[SCrypt])(identity)
}
