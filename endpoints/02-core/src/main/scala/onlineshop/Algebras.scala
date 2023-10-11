package onlineshop

import onlineshop.algebras.Brands
import onlineshop.algebras.Categories
import onlineshop.algebras.Customers
import onlineshop.algebras.Products
import onlineshop.algebras.Users

case class Algebras[F[_]](
    brands: Brands[F],
    products: Products[F],
    categories: Categories[F],
    customers: Customers[F],
    users: Users[F],
  )
