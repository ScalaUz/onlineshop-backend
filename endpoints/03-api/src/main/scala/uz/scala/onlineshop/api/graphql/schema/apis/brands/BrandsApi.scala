package uz.scala.onlineshop.api.graphql.schema.apis.brands

import caliban.GraphQL
import caliban.RootResolver
import caliban.graphQL
import caliban.schema.Schema
import cats.Monad
import cats.effect.std.Dispatcher
import uz.scala.onlineshop.Language
import uz.scala.onlineshop.algebras.BrandsAlgebra
import uz.scala.onlineshop.api.graphql.GraphQLContext
import uz.scala.onlineshop.api.graphql.GraphQLTypes
import uz.scala.onlineshop.api.graphql.schema.GraphqlApi

class BrandsApi[F[_]: Dispatcher, R](queries: Queries[F], mutations: Mutations[F])
    extends GraphqlApi[F, R]
       with GraphQLTypes[GraphQLContext] {
  import Schema.auto._
  import caliban.interop.cats.implicits.catsEffectSchema

  override def graphql: GraphQL[R] =
    graphQL(RootResolver(queries, mutations))
}

object BrandsApi {
  def make[F[_]: Monad: Dispatcher, R](
      brandsAlgebra: BrandsAlgebra[F]
    )(implicit
      lang: Language
    ): GraphqlApi[F, R] =
    new BrandsApi[F, R](
      queries = Queries[F](
        brands = brandsAlgebra.fetchAll,
        brandById = id => brandsAlgebra.findById(id),
      ),
      mutations = Mutations[F](
        createBrand = brandInput => brandsAlgebra.create(brandInput),
        updateBrand = brandInput => brandsAlgebra.update(brandInput),
        deleteBrand = id => brandsAlgebra.delete(id),
      ),
    )
}
