package onlineshop.api.graphql

import sangria.schema.ObjectType

import onlineshop.api.graphql.schema.Ctx
import onlineshop.api.graphql.schema.Val

trait GraphQLApi[F[_]] {
  def queryType: ObjectType[Ctx[F], Val]
}
