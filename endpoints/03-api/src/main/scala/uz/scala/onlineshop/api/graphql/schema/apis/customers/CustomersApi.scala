package uz.scala.onlineshop.api.graphql.schema.apis.customers

import caliban.GraphQL
import caliban.RootResolver
import caliban.graphQL
import caliban.schema.Schema
import cats.Monad
import cats.effect.std.Dispatcher
import cats.implicits.toFunctorOps
import uz.scala.onlineshop.Language
import uz.scala.onlineshop.algebras.CustomersAlgebra
import uz.scala.onlineshop.api.graphql.GraphQLContext
import uz.scala.onlineshop.api.graphql.GraphQLTypes
import uz.scala.onlineshop.api.graphql.schema.GraphqlApi

class CustomersApi[F[_]: Dispatcher, R](queries: Queries[F], mutations: Mutations[F])
    extends GraphqlApi[F, R]
       with GraphQLTypes[GraphQLContext] {
  import Schema.auto._
  import caliban.interop.cats.implicits.catsEffectSchema

  override def graphql: GraphQL[R] =
    graphQL(RootResolver(queries, mutations))
}

object CustomersApi {
  def make[F[_]: Monad: Dispatcher, R](
      customersAlgebra: CustomersAlgebra[F]
    )(implicit
      lang: Language
    ): GraphqlApi[F, R] =
    new CustomersApi[F, R](
      queries = Queries[F](
        customers = customersAlgebra.getCustomers,
        customerById = id => customersAlgebra.findById(id).map(_.map(_.toView)),
      ),
      mutations = Mutations[F](
        updateCustomer = customerInput => customersAlgebra.update(customerInput),
        deleteCustomer = id => customersAlgebra.delete(id),
      ),
    )
}
