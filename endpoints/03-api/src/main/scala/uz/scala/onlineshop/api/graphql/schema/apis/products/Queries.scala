package uz.scala.onlineshop.api.graphql.schema.apis.products

import caliban.interop.cats.FromEffect
import caliban.schema.Annotations.GQLDescription
import caliban.schema.Schema
import uz.scala.onlineshop.api.graphql.GraphQLTypes
import uz.scala.onlineshop.domain.products.Product

case class Queries[F[_]](
    @GQLDescription("Fetch all products")
    products: F[List[Product]]
  )

object Queries extends GraphQLTypes {
  implicit def schema[F[_]]: Schema.Typeclass[Queries[F]] =
    Schema.gen
}
