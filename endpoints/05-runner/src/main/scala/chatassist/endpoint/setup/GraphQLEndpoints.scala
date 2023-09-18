package chatassist.endpoint.setup

import cats.Monad
import cats.effect.std.Dispatcher
import sangria.schema.ObjectType

import onlineshop.api.graphql.GraphQLApi
import onlineshop.api.graphql.schema._

class GraphQLEndpoints[F[_]: Monad: Dispatcher](env: Environment[F]) {
  private val apis: List[GraphQLApi[F]] =
    List(
      new ProductsApi[F](env.repositories.products),
      new CategoriesApi[F](env.repositories.categories),
    )
  val queryType: ObjectType[Ctx[F], Val] =
    ObjectType("Query", apis.flatMap(api => api.queryType.ownFields))
}
