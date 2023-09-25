package onlineshop.api.graphql.schema

import caliban.Value.StringValue
import caliban.parsing.adt.Directive
import caliban.schema.Annotations.GQLDirective

import onlineshop.domain.enums.Role
object Utils {
  val directiveName = "requiredRole"
  val attributeName = "role"

  case class Access(role: Role*)
      extends GQLDirective(
        Directive(
          directiveName,
          Map(attributeName -> StringValue(role.map(_.entryName).mkString(","))),
        )
      )
}
