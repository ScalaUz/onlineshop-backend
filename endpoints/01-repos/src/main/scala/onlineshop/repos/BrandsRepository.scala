package onlineshop.repos

import cats.effect.Async
import cats.effect.Resource
import skunk._
import uz.scala.skunk.syntax.all.skunkSyntaxCommandOps
import uz.scala.skunk.syntax.all.skunkSyntaxQueryVoidOps

import onlineshop.domain.Brand
import onlineshop.repos.sql.BrandsSql

trait BrandsRepository[F[_]] {
  def fetchAll: F[List[Brand]]
  def create(brand: Brand): F[Unit]
}

object BrandsRepository {
  def make[F[_]: Async](
      implicit
      session: Resource[F, Session[F]]
    ): BrandsRepository[F] = new BrandsRepository[F] {
    override def create(brand: Brand): F[Unit] =
      BrandsSql.insert.execute(brand)
    override def fetchAll: F[List[Brand]] =
      BrandsSql.fetch.all
  }
}
