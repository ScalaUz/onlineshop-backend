package onlineshop.api.graphql.schema

import onlineshop.domain.Product

trait GraphQLTypes {
  import caliban.schema._
  implicit val productSchema: Schema[Any, Product] = Schema.gen[Any, Product]
}
