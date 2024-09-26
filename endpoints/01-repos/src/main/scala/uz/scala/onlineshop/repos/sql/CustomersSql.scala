package uz.scala.onlineshop.repos.sql

import skunk._
import skunk.implicits._
import uz.scala.onlineshop.Phone
import uz.scala.onlineshop.domain.CustomerId
import uz.scala.onlineshop.repos.dto.Customer

private[repos] object CustomersSql extends Sql[CustomerId] {
  private val codec = (id *: nes *: phone *: zdt *: zdt.opt *: zdt.opt).to[Customer]

  val findByLogin: Query[Phone, Customer] =
    sql"""SELECT * FROM customers WHERE phone = $phone LIMIT 1""".query(codec)

  val findById: Query[CustomerId, Customer] =
    sql"""SELECT * FROM customers WHERE id = $id LIMIT 1""".query(codec)

  val insert: Command[Customer] =
    sql"""INSERT INTO customers VALUES ($codec)""".command

  val delete: Command[CustomerId] =
    sql"""UPDATE customers SET deleted_at = now() WHERE id = $id""".command

  val update: Command[Customer] =
    sql"""UPDATE customers SET name = $nes, updated_at = now() WHERE id = $id AND deleted_at IS NULL"""
      .command
      .contramap { (u: Customer) =>
        u.name *: u.id *: EmptyTuple
      }

  val select: Query[Void, Customer] =
    sql"""SELECT * FROM customers WHERE deleted_at IS NULL ORDER BY created_at DESC""".query(codec)
}
