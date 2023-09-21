package onlineshop.api.graphql

import onlineshop.api.graphql.schema.ProductsQueries

case class Queries[F[_]](
    products: ProductsQueries[F]
  )
