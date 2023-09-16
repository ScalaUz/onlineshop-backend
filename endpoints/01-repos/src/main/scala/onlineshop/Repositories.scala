package onlineshop

import cats.effect.Async
import cats.effect.Resource
import skunk.Session

import onlineshop.repos.ProductsRepository

case class Repositories[F[_]](
    products: ProductsRepository[F]
  )
object Repositories {
  def make[F[_]: Async](
      implicit
      session: Resource[F, Session[F]]
    ): Repositories[F] =
    Repositories(
      products = ProductsRepository.make[F]
    )
}
