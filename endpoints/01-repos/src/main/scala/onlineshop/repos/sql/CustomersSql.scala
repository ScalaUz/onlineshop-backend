package onlineshop.repos.sql

import shapeless.HNil
import skunk._
import skunk.codec.all.varchar
import skunk.implicits._

import onlineshop.Phone
import onlineshop.domain.AccessCredentials
import onlineshop.domain.AuthedUser.Customer
import onlineshop.domain.PersonId

private[repos] object CustomersSql extends Sql[PersonId] {
  private val codec = (id *: zonedDateTime *: nes *: nes *: phone).to[Customer]
  private val accessCredentialsDecoder: Decoder[AccessCredentials[Customer]] =
    (codec *: passwordHash).map {
      case customer *: hash *: HNil =>
        AccessCredentials(
          data = customer,
          password = hash,
        )
    }

  val findByPhone: Query[Phone, AccessCredentials[Customer]] =
    sql"""SELECT id, created_at, firstname, lastname, phone, password FROM guests
          WHERE phone = $phone LIMIT 1""".query(accessCredentialsDecoder)

  val insert: Command[AccessCredentials[Customer]] =
    sql"""INSERT INTO users VALUES ($id, $zonedDateTime, $nes, $nes, $phone, $passwordHash)"""
      .command
      .contramap { (g: AccessCredentials[Customer]) =>
        g.data.id *: g.data.createdAt *: g.data.firstname *: g.data.lastname *: g.data.phone *:
          g.password *: EmptyTuple
      }
}
