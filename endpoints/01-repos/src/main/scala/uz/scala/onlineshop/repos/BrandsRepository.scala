package uz.scala.onlineshop.repos

import cats.data.OptionT
import cats.effect.Async
import cats.effect.Resource
import skunk._
import uz.scala.onlineshop.Language
import uz.scala.onlineshop.ResponseMessages.BRAND_NOT_FOUND
import uz.scala.onlineshop.domain.BrandId
import uz.scala.onlineshop.exception.AError
import uz.scala.onlineshop.repos.dto.Brand
import uz.scala.onlineshop.repos.sql.BrandsSql
import uz.scala.skunk.syntax.all.skunkSyntaxCommandOps
import uz.scala.skunk.syntax.all.skunkSyntaxQueryOps
import uz.scala.skunk.syntax.all.skunkSyntaxQueryVoidOps

trait BrandsRepository[F[_]] {
  def fetchAll: F[List[Brand]]
  def create(brand: Brand): F[Unit]
  def fetchById(id: BrandId): F[Option[Brand]]
  def update(id: BrandId)(update: Brand => Brand)(implicit lang: Language): F[Unit]
  def delete(id: BrandId): F[Unit]
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

    override def fetchById(id: BrandId): F[Option[Brand]] =
      BrandsSql.fetchById.queryOption(id)

    override def update(id: BrandId)(update: Brand => Brand)(implicit lang: Language): F[Unit] =
      OptionT(fetchById(id))
        .semiflatMap { oldBrand =>
          BrandsSql.update.execute(update(oldBrand))
        }
        .getOrRaise(AError.Internal(BRAND_NOT_FOUND(lang)))

    override def delete(id: BrandId): F[Unit] =
      BrandsSql.delete.execute(id)
  }
}
