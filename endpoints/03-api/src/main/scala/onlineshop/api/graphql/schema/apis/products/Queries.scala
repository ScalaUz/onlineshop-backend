package onlineshop.api.graphql.schema.apis.products

import caliban.interop.cats.implicits._
import caliban.schema.Annotations.GQLDescription
import caliban.schema.Schema

import onlineshop.api.graphql.GraphQLTypes
import onlineshop.api.graphql.SchemaF
import onlineshop.domain.Product

case class Queries[F[_]](
    @GQLDescription("Fetch all products")
    products: F[List[Product]]
  )

object Queries extends GraphQLTypes {
  implicit def schemaF[F[_]]: SchemaF[F, Any, Queries] =
    SchemaF(implicit eff => Schema.gen)
}
