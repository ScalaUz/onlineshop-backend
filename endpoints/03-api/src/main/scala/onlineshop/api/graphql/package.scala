package onlineshop.api

import caliban.CalibanError.ExecutionError
import caliban.ResponseValue
import caliban.Value.StringValue
import caliban.execution.FieldInfo
import caliban.parsing.adt.Directive
import caliban.wrappers.Wrapper.FieldWrapper
import izumi.reflect.TagK
import zio.ZIO
import zio.query.ZQuery

import onlineshop.api.graphql.schema.Utils.attributeName
import onlineshop.api.graphql.schema.Utils.directiveName
import onlineshop.domain.enums.Role
package object graphql {

  /** Returns a wrapper that check directives on fields and can potentially fail the query
    *
    * @param check a function from directives to a ZIO that can fail
    */
  private def checkDirectives[R](
      check: List[Directive] => ZIO[R, ExecutionError, Unit]
    ): FieldWrapper[R] =
    new FieldWrapper[R]() {
      def wrap[R1 <: R](
          query: ZQuery[R1, ExecutionError, ResponseValue],
          info: FieldInfo,
        ): ZQuery[R1, ExecutionError, ResponseValue] = {
        val directives = info
          .parent
          .flatMap(_.allFields.find(_.name == info.name))
          .flatMap(_.directives)
          .getOrElse(Nil)
        ZQuery.fromZIO(check(directives)) *> query
      }
    }
  def authWrapper[F[_]: TagK]: FieldWrapper[GraphQLContext[F]] =
    checkDirectives(directives =>
      for {
        currentRole <- ZIO.serviceWith[GraphQLContext[F]](
          _.authInfo.map(_.role)
        )
        restrictedRoles = directives
          .find(_.name == directiveName)
          .flatMap(_.arguments.get(attributeName))
          .collectFirst {
            case StringValue(roles) =>
              roles.split(",").flatMap(role => Role.values.find(_.entryName == role)).toList
          }
          .getOrElse(Nil)
        _ <- ZIO.unless(restrictedRoles.isEmpty || restrictedRoles.exists(currentRole.contains))(
          ZIO.fail(ExecutionError("Unauthorized"))
        )
      } yield ()
    )
}
