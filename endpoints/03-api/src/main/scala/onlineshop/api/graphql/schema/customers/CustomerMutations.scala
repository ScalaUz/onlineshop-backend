package onlineshop.api.graphql.schema.customers

import caliban.schema.Annotations.GQLName

import onlineshop.algebras.Customers
import onlineshop.domain.PersonId
import onlineshop.domain.args.CustomerInfo

@GQLName("Customer")
case class CustomerMutations[F[_]](
    create: CustomerInfo => F[PersonId]
  )

object CustomerMutations {
  def make[F[_]](
      customersAlgebra: Customers[F]
    ): CustomerMutations[F] =
    CustomerMutations[F](
      create = customerInfo => customersAlgebra.create(customerInfo)
    )
}
