package uz.scala.onlineshop.api.graphql.schema.apis.users

import caliban.GraphQL
import caliban.RootResolver
import caliban.graphQL
import caliban.schema.Schema
import cats.Monad
import cats.effect.std.Dispatcher
import cats.implicits.toFunctorOps
import uz.scala.onlineshop.Language
import uz.scala.onlineshop.algebras.UsersAlgebra
import uz.scala.onlineshop.api.graphql.GraphQLContext
import uz.scala.onlineshop.api.graphql.GraphQLTypes
import uz.scala.onlineshop.api.graphql.schema.GraphqlApi

class UsersApi[F[_]: Dispatcher, R](queries: Queries[F], mutations: Mutations[F])
    extends GraphqlApi[F, R]
       with GraphQLTypes[GraphQLContext] {
  import Schema.auto._
  import caliban.interop.cats.implicits.catsEffectSchema

  override def graphql: GraphQL[R] =
    graphQL(RootResolver(queries, mutations))
}

object UsersApi {
  def make[F[_]: Monad: Dispatcher, R](
      usersAlgebra: UsersAlgebra[F]
    )(implicit
      lang: Language
    ): UsersApi[F, R] =
    new UsersApi[F, R](
      queries = Queries[F](
        users = usersAlgebra.getUsers,
        userById = id => usersAlgebra.findById(id).map(_.map(_.toDomain)),
      ),
      mutations = Mutations[F](
        createUser = userInput => usersAlgebra.create(userInput),
        updateUser = userUpdateInput => usersAlgebra.update(userUpdateInput),
        deleteUser = id => usersAlgebra.delete(id),
      ),
    )
}
