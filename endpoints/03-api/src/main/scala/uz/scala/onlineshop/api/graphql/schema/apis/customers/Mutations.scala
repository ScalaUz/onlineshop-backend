package uz.scala.onlineshop.api.graphql.schema.apis.customers

import caliban.schema.Annotations.GQLDescription
import uz.scala.onlineshop.domain.CustomerId
import uz.scala.onlineshop.domain.customers.CustomerUpdateInput

case class Mutations[F[_]](
    @GQLDescription("Update Customer")
    updateCustomer: CustomerUpdateInput => F[Unit],
    @GQLDescription("Delete Customer")
    deleteCustomer: CustomerId => F[Unit],
  )
