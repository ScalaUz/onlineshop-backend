package uz.scala.mailer

import eu.timepit.refined.types.net.SystemPortNumber
import eu.timepit.refined.types.string.NonEmptyString
import uz.scala.mailer.MailerConfig.SenderAndRecipients
import uz.scala.mailer.data.types.Host
import uz.scala.mailer.data.types.Password
import uz.scala.onlineshop.EmailAddress

case class MailerConfig(
    enabled: Boolean,
    host: Host,
    port: SystemPortNumber,
    username: NonEmptyString,
    password: Password,
    fromAddress: EmailAddress,
    recipients: List[EmailAddress],
  ) {
  def toSenderAndRecipients: SenderAndRecipients =
    SenderAndRecipients(fromAddress, recipients)
}

object MailerConfig {
  case class SenderAndRecipients(fromAddress: EmailAddress, recipients: List[EmailAddress])
}
