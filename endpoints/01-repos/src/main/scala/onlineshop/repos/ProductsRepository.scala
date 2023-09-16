package onlineshop.repos

import cats.effect.Async
import cats.effect.Resource
import skunk._
import uz.scala.skunk.syntax.all.skunkSyntaxQueryVoidOps

import onlineshop.domain.Product
import onlineshop.repos.sql.ProductsSql

trait ProductsRepository[F[_]] {
  def fetchAll: F[List[Product]]
}

object ProductsRepository {
  def make[F[_]: Async](
      implicit
      session: Resource[F, Session[F]]
    ): ProductsRepository[F] = new ProductsRepository[F] {
    override def fetchAll: F[List[Product]] =
      ProductsSql.fetch.all
  }
}
