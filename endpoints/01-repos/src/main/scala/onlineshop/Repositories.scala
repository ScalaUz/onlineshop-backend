package onlineshop

import cats.effect.Async
import cats.effect.Resource
import skunk.Session

import onlineshop.repos.CategoriesRepository
import onlineshop.repos.CustomersRepository
import onlineshop.repos.ProductsRepository
import onlineshop.repos.UsersRepository

case class Repositories[F[_]](
    products: ProductsRepository[F],
    categories: CategoriesRepository[F],
    customers: CustomersRepository[F],
    users: UsersRepository[F],
  )
object Repositories {
  def make[F[_]: Async](
      implicit
      session: Resource[F, Session[F]]
    ): Repositories[F] =
    Repositories(
      products = ProductsRepository.make[F],
      categories = CategoriesRepository.make[F],
      customers = CustomersRepository.make[F],
      users = UsersRepository.make[F],
    )
}
