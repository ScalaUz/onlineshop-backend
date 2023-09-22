package onlineshop.api.graphql

import caliban.schema.Annotations.GQLName

import onlineshop.api.graphql.schema.CategoriesQueries
import onlineshop.api.graphql.schema.ProductsQueries

@GQLName("Queries")
case class Queries[F[_]](
    products: ProductsQueries[F],
    categories: CategoriesQueries[F],
  )
