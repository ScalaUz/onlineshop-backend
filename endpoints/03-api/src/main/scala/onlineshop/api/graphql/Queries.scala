package onlineshop.api.graphql

import caliban.schema.Annotations.GQLName

import onlineshop.Algebras
import onlineshop.api.graphql.schema.categories.CategoriesQueries
import onlineshop.api.graphql.schema.products.ProductsQueries

@GQLName("Queries")
case class Queries[F[_]](
    products: ProductsQueries[F],
    categories: CategoriesQueries[F],
  )

object Queries {
  def make[F[_]](algebras: Algebras[F]): Queries[F] =
    Queries[F](
      products = ProductsQueries.make[F](algebras.products),
      categories = CategoriesQueries.make[F](algebras.categories),
    )
}
