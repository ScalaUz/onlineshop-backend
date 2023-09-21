package onlineshop

import onlineshop.algebras.Products

case class Algebras[F[_]](products: Products[F])
