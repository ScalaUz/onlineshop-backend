package onlineshop.algebras

import onlineshop.domain.Product
import onlineshop.repos.ProductsRepository
trait ProductsAlgebra[F[_]] {
  def fetchAll: F[List[Product]]
}
object ProductsAlgebra {
  def make[F[_]](productsRepository: ProductsRepository[F]): ProductsAlgebra[F] =
    new ProductsAlgebra[F] {
      override def fetchAll: F[List[Product]] = productsRepository.fetchAll
    }
}
