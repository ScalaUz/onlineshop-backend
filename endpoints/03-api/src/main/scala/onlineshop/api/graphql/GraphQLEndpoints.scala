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
import izumi.reflect.TagK
import zio.Runtime
import zio.ZEnvironment
import zio.durationInt

import onlineshop.Algebras
import onlineshop.api.graphql.schema.CategoriesQueries
import onlineshop.api.graphql.schema.ProductsQueries
import onlineshop.domain.AuthedUser

class GraphQLEndpoints[F[_]: TagK: Async](
    algebras: Algebras[F],
    maybeUser: Option[AuthedUser],
  )(implicit
    dispatcher: Dispatcher[F]
  ) {
  private val Algebras(products, categories) = algebras
  private lazy val graphQLContext: GraphQLContext[F] =
    GraphQLContext[F](authInfo = maybeUser, algebras = algebras)
  implicit val runtime: Runtime[GraphQLContext[F]] =
    Runtime.default.withEnvironment(ZEnvironment(graphQLContext))
  implicit val interop: CatsInterop[F, GraphQLContext[F]] =
    CatsInterop.default[F, GraphQLContext[F]](dispatcher)
  private def query: Queries[F] = Queries[F](
    products = ProductsQueries.make[F](products),
    categories = CategoriesQueries.make[F](categories),
  )

  def createGraphQL: GraphQL[GraphQLContext[F]] = {
    implicit val schema: GenericSchema[GraphQLContext[F]] = new GenericSchema[GraphQLContext[F]] {}
    import schema.auto._
    graphQL(RootResolver(query)) @@
      maxDepth(50) @@
      timeout(3.seconds) @@
      printSlowQueries(500.millis) @@
      authWrapper @@
      apolloCaching @@
      printErrors
  }
}
