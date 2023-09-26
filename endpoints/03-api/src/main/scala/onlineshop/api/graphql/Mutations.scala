package onlineshop.api.graphql

import caliban.schema.Annotations.GQLName

import onlineshop.Algebras
import onlineshop.api.graphql.schema.customers.CustomerMutations
import onlineshop.api.graphql.schema.users.UsersMutations

@GQLName("Mutations")
case class Mutations[F[_]](
    customers: CustomerMutations[F],
    users: UsersMutations[F],
  )

object Mutations extends GraphQLTypes {
  def make[F[_]](algebras: Algebras[F]): Mutations[F] =
    Mutations[F](
      customers = CustomerMutations.make[F](algebras.customers),
      users = UsersMutations.make[F](algebras.users),
    )
}
