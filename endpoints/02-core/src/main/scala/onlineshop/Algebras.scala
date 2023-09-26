package onlineshop

import onlineshop.algebras.Categories
import onlineshop.algebras.Customers
import onlineshop.algebras.Products
import onlineshop.algebras.Users

case class Algebras[F[_]](
    products: Products[F],
    categories: Categories[F],
    customers: Customers[F],
    users: Users[F],
  )
