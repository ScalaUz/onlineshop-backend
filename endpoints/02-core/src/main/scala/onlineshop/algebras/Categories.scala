package onlineshop.algebras

import onlineshop.domain.Category
import onlineshop.repos.CategoriesRepository
trait Categories[F[_]] {
  def fetchAll: F[List[Category]]
}
object Categories {
  def make[F[_]](categoriesRepository: CategoriesRepository[F]): Categories[F] =
    new Categories[F] {
      override def fetchAll: F[List[Category]] = categoriesRepository.fetchAll
    }
}
