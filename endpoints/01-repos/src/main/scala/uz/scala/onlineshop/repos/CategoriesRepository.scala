package uz.scala.onlineshop.repos

import cats.data.OptionT
import cats.effect.Async
import cats.effect.Resource
import skunk._
import uz.scala.onlineshop.Language
import uz.scala.onlineshop.ResponseMessages.CATEGORY_NOT_FOUND
import uz.scala.onlineshop.domain.CategoryId
import uz.scala.onlineshop.exception.AError
import uz.scala.onlineshop.repos.dto.Category
import uz.scala.onlineshop.repos.sql.CategoriesSql
import uz.scala.skunk.syntax.all.skunkSyntaxCommandOps
import uz.scala.skunk.syntax.all.skunkSyntaxQueryOps
import uz.scala.skunk.syntax.all.skunkSyntaxQueryVoidOps

trait CategoriesRepository[F[_]] {
  def fetchAll: F[List[Category]]
  def create(category: Category): F[Unit]
  def update(
      id: CategoryId
    )(
      update: Category => Category
    )(implicit
      lang: Language
    ): F[Unit]
  def delete(id: CategoryId): F[Unit]
  def fetchById(id: CategoryId): F[Option[Category]]
}

object CategoriesRepository {
  def make[F[_]: Async](
      implicit
      session: Resource[F, Session[F]]
    ): CategoriesRepository[F] = new CategoriesRepository[F] {
    override def create(category: Category): F[Unit] =
      CategoriesSql.insert.execute(category)

    override def fetchAll: F[List[Category]] =
      CategoriesSql.fetch.all

    override def update(
        id: CategoryId
      )(
        update: Category => Category
      )(implicit
        lang: Language
      ): F[Unit] =
      OptionT(fetchById(id))
        .semiflatMap { oldCategory =>
          CategoriesSql.update.execute(update(oldCategory))
        }
        .getOrRaise(AError.Internal(CATEGORY_NOT_FOUND(lang)))

    override def delete(id: CategoryId): F[Unit] =
      CategoriesSql.delete.execute(id)

    override def fetchById(id: CategoryId): F[Option[Category]] =
      CategoriesSql.fetchById.queryOption(id)
  }
}
