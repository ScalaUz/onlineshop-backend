package uz.scala.onlineshop.api.graphql.schema

import caliban.Value.StringValue
import caliban.parsing.adt.Directive
import caliban.schema.Annotations.GQLDirective
import uz.scala.onlineshop.domain.enums.Role
object Utils {
  val directiveName = "requiredPrivilege"
  val attributeName = "privilege"

  case class Authorized(privilege: Role*)
      extends GQLDirective(
        Directive(
          directiveName,
          Map(attributeName -> StringValue(privilege.map(_.entryName).mkString(","))),
        )
      )
}
