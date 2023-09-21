package chatassist.endpoint.setup

import caliban.GraphQL
import caliban.RootResolver
import caliban.graphQL
import caliban.interop.cats.CatsInterop
import caliban.schema.GenericSchema
import cats.MonadThrow
import izumi.reflect.TagK

import onlineshop.Algebras
import onlineshop.api.graphql.GraphQLContext
import onlineshop.api.graphql.Queries
import onlineshop.api.graphql.schema.ProductsQueries
class GraphQLEndpoints[F[_]: TagK: MonadThrow](
    algebras: Algebras[F]
  )(implicit
    interop: CatsInterop[F, GraphQLContext[F]]
  ) {
  private val Algebras(products) = algebras

  private def query: Queries[F] = Queries[F](
    products = ProductsQueries.make[F](products)
  )

  def createGraphQL: GraphQL[GraphQLContext[F]] = {
    implicit val schema: GenericSchema[GraphQLContext[F]] = new GenericSchema[GraphQLContext[F]] {}

    import caliban.interop.cats.implicits._
    import schema.auto._
    graphQL(RootResolver(query))
  }
}
