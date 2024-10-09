package uz.scala.onlineshop.api.graphql.schema.apis.customers

import caliban.GraphQL
import caliban.RootResolver
import caliban.graphQL
import caliban.interop.cats.CatsInterop
import cats.Monad
import cats.effect.std.Dispatcher
import cats.implicits.toFunctorOps
import uz.scala.onlineshop.Language
import uz.scala.onlineshop.algebras.AddressesAlgebra
import uz.scala.onlineshop.algebras.CustomersAlgebra
import uz.scala.onlineshop.api.graphql.{GraphQLContext, GraphQLTypes}
import uz.scala.onlineshop.api.graphql.schema.GraphqlApi
import uz.scala.onlineshop.api.graphql.views.Customer

class CustomersApi[F[_]: Dispatcher](queries: Queries[F], mutations: Mutations[F])
    extends GraphqlApi[F, GraphQLContext]
       with GraphQLTypes[GraphQLContext] {
  import auto._
  import caliban.interop.cats.implicits._

  override def graphql: GraphQL[GraphQLContext] =
    graphQL(RootResolver(queries, mutations))
}

object CustomersApi {
  def make[F[_]: Monad: Dispatcher, R](
    )(implicit
      customersAlgebra: CustomersAlgebra[F],
      addressesAlgebra: AddressesAlgebra[F],
      lang: Language,
      interop: CatsInterop[F, GraphQLContext],
    ): GraphqlApi[F, GraphQLContext] =
    new CustomersApi[F](
      queries = Queries[F](
        customers = customersAlgebra.getCustomers.map(_.map(Customer.fromDomain[F])),
        customerById = id =>
          customersAlgebra.findById(id).map(_.map(_.toView)).map(_.map(Customer.fromDomain[F])),
      ),
      mutations = Mutations[F](
        updateCustomer = customerInput => customersAlgebra.update(customerInput),
        deleteCustomer = id => customersAlgebra.delete(id),
      ),
    )
}
