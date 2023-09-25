package onlineshop.api.graphql

import caliban.schema.Annotations.GQLName

import onlineshop.api.graphql.schema.CustomerMutations

@GQLName("Mutations")
case class Mutations[F[_]](
    customers: CustomerMutations[F]
  )
