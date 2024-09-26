package uz.scala.onlineshop.repos

import cats.effect.Async
import cats.effect.Resource
import skunk._
import uz.scala.onlineshop.repos.dto.Category
import uz.scala.skunk.syntax.all.skunkSyntaxCommandOps
import uz.scala.skunk.syntax.all.skunkSyntaxQueryVoidOps
import uz.scala.onlineshop.repos.sql.CategoriesSql

trait CategoriesRepository[F[_]] {
  def fetchAll: F[List[Category]]
  def create(category: Category): F[Unit]
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
  }
}
