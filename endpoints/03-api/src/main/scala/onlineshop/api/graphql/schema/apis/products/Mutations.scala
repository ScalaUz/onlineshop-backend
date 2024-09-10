package onlineshop.api.graphql.schema.apis.products

import caliban.schema.Schema

import onlineshop.api.graphql.SchemaF

case class Mutations[F[_]]()

object Mutations {
  implicit def schemaF[F[_]]: SchemaF[F, Any, Mutations] =
    SchemaF(implicit eff => Schema.gen)
}
