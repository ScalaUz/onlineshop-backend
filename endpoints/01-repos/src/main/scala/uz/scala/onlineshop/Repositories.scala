package uz.scala.onlineshop

import cats.effect.Async
import cats.effect.Resource
import skunk.Session
import uz.scala.onlineshop.repos._

case class Repositories[F[_]](
    products: ProductsRepository[F],
    categories: CategoriesRepository[F],
    brands: BrandsRepository[F],
    customers: CustomersRepository[F],
    users: UsersRepository[F],
    assets: AssetsRepository[F],
    addresses: AddressesRepository[F],
  )
object Repositories {
  def make[F[_]: Async](
      implicit
      session: Resource[F, Session[F]]
    ): Repositories[F] =
    Repositories(
      products = ProductsRepository.make[F],
      categories = CategoriesRepository.make[F],
      brands = BrandsRepository.make[F],
      customers = CustomersRepository.make[F],
      users = UsersRepository.make[F],
      assets = AssetsRepository.make[F],
      addresses = AddressesRepository.make[F],
    )
}
