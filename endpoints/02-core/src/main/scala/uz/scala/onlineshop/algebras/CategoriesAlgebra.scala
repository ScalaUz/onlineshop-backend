package uz.scala.onlineshop.algebras

import cats.Monad
import cats.implicits.toFlatMapOps
import cats.implicits.toFunctorOps
import uz.scala.onlineshop.Language
import uz.scala.onlineshop.domain.CategoryId
import uz.scala.onlineshop.domain.categories.Category
import uz.scala.onlineshop.domain.categories.CategoryInput
import uz.scala.onlineshop.effects.GenUUID
import uz.scala.onlineshop.repos.CategoriesRepository
import uz.scala.onlineshop.repos.dto
import uz.scala.onlineshop.utils.ID

trait CategoriesAlgebra[F[_]] {
  def create(categoryInput: CategoryInput): F[CategoryId]
  def fetchAll(implicit lang: Language): F[List[Category]]
}
object CategoriesAlgebra {
  def make[F[_]: Monad: GenUUID](
      categoriesRepository: CategoriesRepository[F]
    ): CategoriesAlgebra[F] =
    new CategoriesAlgebra[F] {
      override def create(categoryInput: CategoryInput): F[CategoryId] =
        for {
          catId <- ID.make[F, CategoryId]
          category = dto.Category(
            id = catId,
            nameUz = categoryInput.nameUz,
            nameRu = categoryInput.nameRu,
            nameEn = categoryInput.nameEn,
          )
          _ <- categoriesRepository.create(category)
        } yield catId

      override def fetchAll(implicit lang: Language): F[List[Category]] =
        categoriesRepository.fetchAll.map(_.map(_.toView))
    }
}
