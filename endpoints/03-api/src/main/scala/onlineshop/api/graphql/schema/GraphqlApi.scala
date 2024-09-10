package onlineshop.api.graphql.schema

import caliban._
import caliban.interop.cats.FromEffect

import onlineshop.api.graphql.SchemaF

abstract class GraphqlApi[F[_], Env, +Queries[_[_]], +Mutations[_[_]]](
    queries: Option[Queries[F]] = None,
    mutations: Option[Mutations[F]] = None,
  )(implicit
    qSchema: SchemaF[F, Env, Queries],
    mSchema: SchemaF[F, Env, Mutations],
  ) {
  import mSchema.{ schema => mSchema1 }
  import qSchema.{ schema => qSchema1 }

  final def graphql(
      implicit
      effect: FromEffect[F, Env]
    ): GraphQL[Env] =
    graphQL(RootResolver(queries, mutations))
}

object GraphqlApi {
  type Any[F[_], Env] = GraphqlApi[F, Env, AnyF, AnyF]

  protected type AnyF[F[_]] = scala.Any
}
