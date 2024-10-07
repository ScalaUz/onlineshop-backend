package uz.scala.onlineshop.api.graphql.schema.apis.customers

import caliban.schema.Annotations.GQLDescription
import uz.scala.onlineshop.domain.CustomerId
import uz.scala.onlineshop.domain.customers.Customer

case class Queries[F[_]](
    @GQLDescription("Fetch all customers")
    customers: F[List[Customer]],
    @GQLDescription("Fetch customer by id")
    customerById: CustomerId => F[Option[Customer]],
  )
