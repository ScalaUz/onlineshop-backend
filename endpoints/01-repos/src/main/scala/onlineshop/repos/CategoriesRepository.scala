package onlineshop.repos

import cats.effect.Async
import cats.effect.Resource
import skunk._
import uz.scala.skunk.syntax.all.skunkSyntaxQueryVoidOps

import onlineshop.domain.Category
import onlineshop.repos.sql.CategoriesSql

trait CategoriesRepository[F[_]] {
  def fetchAll: F[List[Category]]
}

object CategoriesRepository {
  def make[F[_]: Async](
      implicit
      session: Resource[F, Session[F]]
    ): CategoriesRepository[F] = new CategoriesRepository[F] {
    override def fetchAll: F[List[Category]] =
      CategoriesSql.fetch.all
  }
}
