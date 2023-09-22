package onlineshop

import onlineshop.algebras.Categories
import onlineshop.algebras.Products

case class Algebras[F[_]](products: Products[F], categories: Categories[F])
