package onlineshop.api.graphql.schema

import scala.concurrent.Future

import cats.Monad
import cats.effect.std.Dispatcher
import sangria.macros.derive.GraphQLField
import sangria.macros.derive.deriveContextObjectType
import sangria.schema.Context
import sangria.schema.ObjectType

import onlineshop.api.graphql.GraphQLApi
import onlineshop.domain.Category
import onlineshop.repos.CategoriesRepository

class CategoriesApi[F[_]: Monad](
    categoriesRepo: CategoriesRepository[F]
  )(implicit
    dispatcher: Dispatcher[F]
  ) extends GraphQLApi[F] {
  class Queries {
    @GraphQLField
    def categories(
        ctx: Context[Ctx[F], Val]
      ): Future[List[Category]] =
      dispatcher.unsafeToFuture(categoriesRepo.fetchAll)
  }

  override def queryType: ObjectType[Ctx[F], Val] =
    deriveContextObjectType[Ctx[F], Queries, Val](_ => new Queries)
}
