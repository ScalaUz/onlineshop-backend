package onlineshop.algebras

import cats.Monad
import cats.implicits.toFlatMapOps
import cats.implicits.toFunctorOps

import onlineshop.domain.Category
import onlineshop.domain.CategoryId
import onlineshop.domain.args.CategoryInput
import onlineshop.effects.GenUUID
import onlineshop.repos.CategoriesRepository
import onlineshop.utils.ID
trait Categories[F[_]] {
  def create(categoryInput: CategoryInput): F[CategoryId]
  def fetchAll: F[List[Category]]
}
object Categories {
  def make[F[_]: Monad: GenUUID](categoriesRepository: CategoriesRepository[F]): Categories[F] =
    new Categories[F] {
      override def create(categoryInput: CategoryInput): F[CategoryId] =
        for {
          catId <- ID.make[F, CategoryId]
          category = Category(
            id = catId,
            nameUz = categoryInput.nameUz,
            nameRu = categoryInput.nameRu,
            nameEn = categoryInput.nameEn,
          )
          _ <- categoriesRepository.create(category)
        } yield catId

      override def fetchAll: F[List[Category]] = categoriesRepository.fetchAll
    }
}
