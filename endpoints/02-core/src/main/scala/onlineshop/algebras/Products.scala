package onlineshop.algebras

import onlineshop.domain.Product
import onlineshop.repos.ProductsRepository
trait Products[F[_]] {
  def fetchAll: F[List[Product]]
}
object Products {
  def make[F[_]](productsRepository: ProductsRepository[F]): Products[F] =
    new Products[F] {
      override def fetchAll: F[List[Product]] = productsRepository.fetchAll
    }
}
