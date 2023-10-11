package onlineshop.api.graphql

import caliban.schema.Annotations.GQLName
import onlineshop.Algebras
import onlineshop.api.graphql.schema.brands.BrandsQueries
import onlineshop.api.graphql.schema.categories.CategoriesQueries
import onlineshop.api.graphql.schema.products.ProductsQueries

@GQLName("Queries")
case class Queries[F[_]](
    brands: BrandsQueries[F],
    products: ProductsQueries[F],
    categories: CategoriesQueries[F],
  )

object Queries {
  def make[F[_]](algebras: Algebras[F]): Queries[F] =
    Queries[F](
      brands = BrandsQueries.make[F](algebras.brands),
      products = ProductsQueries.make[F](algebras.products),
      categories = CategoriesQueries.make[F](algebras.categories),
    )
}
