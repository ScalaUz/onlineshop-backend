package onlineshop.api.graphql

import caliban.GraphQL
import caliban.RootResolver
import caliban.graphQL
import caliban.interop.cats.CatsInterop
import caliban.interop.cats.implicits._
import caliban.schema.GenericSchema
import caliban.wrappers.ApolloCaching.apolloCaching
import caliban.wrappers.Wrappers._
import cats.effect.Async
import cats.effect.std.Dispatcher
import zio.Runtime
import zio.ZEnvironment
import zio.durationInt

import onlineshop.Algebras
import onlineshop.domain.AuthedUser

class GraphQLEndpoints[F[_]: Async](
    algebras: Algebras[F],
    maybeUser: Option[AuthedUser],
  )(implicit
    dispatcher: Dispatcher[F]
  ) extends GraphQLTypes {
  private lazy val graphQLContext: GraphQLContext =
    GraphQLContext(authInfo = maybeUser)
  implicit val runtime: Runtime[GraphQLContext] =
    Runtime.default.withEnvironment(ZEnvironment(graphQLContext))
  implicit val interop: CatsInterop[F, GraphQLContext] =
    CatsInterop.default[F, GraphQLContext](dispatcher)
  private val query: Queries[F] = Queries.make[F](algebras)
  private val mutations: Mutations[F] = Mutations.make[F](algebras)

  def createGraphQL: GraphQL[GraphQLContext] = {
    implicit val schema: GenericSchema[GraphQLContext] = new GenericSchema[GraphQLContext] {}
    import schema.auto._
    graphQL(RootResolver(query, mutations)) @@
      maxDepth(50) @@
      timeout(3.seconds) @@
      printSlowQueries(500.millis) @@
      authWrapper @@
      apolloCaching @@
      printErrors
  }
}
