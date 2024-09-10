package onlineshop.api

import caliban.CalibanError.ExecutionError
import caliban.Value.StringValue
import caliban.wrappers.Wrapper.FieldWrapper
import caliban.wrappers.Wrappers.checkDirectives
import zio.ZIO
import onlineshop.api.graphql.schema.Utils.attributeName
import onlineshop.api.graphql.schema.Utils.directiveName
import onlineshop.domain.enums.Role
package object graphql {

  def authWrapper: FieldWrapper[GraphQLContext] =
    checkDirectives(directives =>
      for {
        userPrivileges <- ZIO.serviceWith[GraphQLContext](
          _.authInfo.map(_.role)
        )
        restrictedPrivileges = directives
          .find(_.name == directiveName)
          .flatMap(_.arguments.get(attributeName))
          .collectFirst {
            case StringValue(privileges) =>
              privileges
                .split(",")
                .flatMap(privilege => Role.values.find(_.entryName == privilege))
                .toList
          }
          .getOrElse(Nil)
        _ <- ZIO.unless(
          restrictedPrivileges.isEmpty || restrictedPrivileges
            .intersect(userPrivileges.toList)
            .nonEmpty
        )(
          ZIO.fail(ExecutionError("User doesn't have correct privilege"))
        )
      } yield {}
    )
}
