package uz.scala.onlineshop.api.graphql.schema.apis.products

import caliban.GraphQL
import caliban.RootResolver
import caliban.graphQL
import caliban.schema.Schema
import cats.Monad
import cats.effect.std.Dispatcher
import uz.scala.onlineshop.Language
import uz.scala.onlineshop.algebras.ProductsAlgebra
import uz.scala.onlineshop.api.graphql.GraphQLContext
import uz.scala.onlineshop.api.graphql.GraphQLTypes
import uz.scala.onlineshop.api.graphql.schema.GraphqlApi

class ProductsApi[F[_]: Dispatcher, R](queries: Queries[F])
    extends GraphqlApi[F, R]
       with GraphQLTypes[GraphQLContext] {
  import Schema.auto._
  import caliban.interop.cats.implicits.catsEffectSchema

  override def graphql: GraphQL[R] =
    graphQL(RootResolver(queries))
}

object ProductsApi {
  def make[F[_]: Monad: Dispatcher, R](
      productsAlgebra: ProductsAlgebra[F]
    )(implicit
      lang: Language
    ): GraphqlApi[F, R] =
    new ProductsApi[F, R](
      queries = Queries[F](
        products = productsAlgebra.fetchAll
      )
    )
}
