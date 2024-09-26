package uz.scala.onlineshop.repos

import java.time.ZonedDateTime

import eu.timepit.refined.types.numeric.NonNegInt
import eu.timepit.refined.types.string.NonEmptyString
import skunk.Codec
import skunk.codec.all._
import skunk.data.Type
import squants.Money
import squants.market.USD
import tsec.passwordhashers.PasswordHash
import tsec.passwordhashers.jca.SCrypt
import uz.scala.onlineshop.EmailAddress
import uz.scala.onlineshop.Phone
import uz.scala.onlineshop.domain.enums.Role
import uz.scala.onlineshop.effects.IsUUID
import uz.scala.syntax.refined.commonSyntaxAutoRefineV

package object sql {
  def identification[A: IsUUID]: Codec[A] = uuid.imap[A](IsUUID[A].uuid.get)(IsUUID[A].uuid.apply)

  val nes: Codec[NonEmptyString] = varchar.imap[NonEmptyString](identity(_))(_.value)
  val nni: Codec[NonNegInt] = int4.imap[NonNegInt](identity(_))(_.value)
  val phone: Codec[Phone] = varchar.imap[Phone](identity(_))(_.value)
  val zdt: Codec[ZonedDateTime] = timestamptz.imap(_.toZonedDateTime)(_.toOffsetDateTime)
  val role: Codec[Role] = `enum`[Role](Role, Type("role"))
  val price: Codec[Money] = numeric.imap[Money](money => USD(money))(_.amount)
  val hash: Codec[PasswordHash[SCrypt]] =
    varchar.imap[PasswordHash[SCrypt]](PasswordHash[SCrypt])(identity)
  val email: Codec[EmailAddress] = varchar.imap[EmailAddress](identity(_))(_.value)
}
