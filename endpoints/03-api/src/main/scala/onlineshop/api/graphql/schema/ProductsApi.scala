package onlineshop.api.graphql.schema

import scala.concurrent.Future

import cats.effect.Sync
import cats.effect.std.Dispatcher
import sangria.macros.derive.GraphQLField
import sangria.macros.derive.deriveContextObjectType
import sangria.schema.Context
import sangria.schema.ObjectType

import onlineshop.api.graphql.Ctx
import onlineshop.api.graphql._
import onlineshop.domain.Product
class ProductsApi[F[_]: Sync](implicit dispatcher: Dispatcher[F]) {
  class Queries {
    @GraphQLField
    def products(
        ctx: Context[Ctx[F], Unit]
      ): Future[List[Product]] =
      dispatcher.unsafeToFuture(ctx.ctx.products.fetchAll)
  }

  def queryType: ObjectType[Ctx[F], Unit] =
    deriveContextObjectType[Ctx[F], Queries, Unit](_ => new Queries)
}
