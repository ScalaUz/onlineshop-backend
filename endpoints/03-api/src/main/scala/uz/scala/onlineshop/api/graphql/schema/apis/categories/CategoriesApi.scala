package uz.scala.onlineshop.api.graphql.schema.apis.categories

import caliban.GraphQL
import caliban.RootResolver
import caliban.graphQL
import caliban.schema.Schema
import cats.Monad
import cats.effect.std.Dispatcher
import uz.scala.onlineshop.Language
import uz.scala.onlineshop.algebras.CategoriesAlgebra
import uz.scala.onlineshop.api.graphql.GraphQLContext
import uz.scala.onlineshop.api.graphql.GraphQLTypes
import uz.scala.onlineshop.api.graphql.schema.GraphqlApi

class CategoriesApi[F[_]: Dispatcher, R](queries: Queries[F], mutations: Mutations[F])
    extends GraphqlApi[F, R]
       with GraphQLTypes[GraphQLContext] {
  import Schema.auto._
  import caliban.interop.cats.implicits.catsEffectSchema

  override def graphql: GraphQL[R] =
    graphQL(RootResolver(queries, mutations))
}

object CategoriesApi {
  def make[F[_]: Monad: Dispatcher, R](
      categoriesAlgebra: CategoriesAlgebra[F]
    )(implicit
      lang: Language
    ): GraphqlApi[F, R] =
    new CategoriesApi[F, R](
      queries = Queries[F](
        categories = categoriesAlgebra.fetchAll,
        categoryById = id => categoriesAlgebra.findById(id),
      ),
      mutations = Mutations[F](
        createCategory = categoryInput => categoriesAlgebra.create(categoryInput),
        updateCategory = categoryUpdateInput => categoriesAlgebra.update(categoryUpdateInput),
        deleteCategory = id => categoriesAlgebra.delete(id),
      ),
    )
}
