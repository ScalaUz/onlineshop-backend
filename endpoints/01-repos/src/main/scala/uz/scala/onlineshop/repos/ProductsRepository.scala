package uz.scala.onlineshop.repos

import cats.data.OptionT
import cats.effect.Async
import cats.effect.Resource
import skunk._
import uz.scala.onlineshop.Language
import uz.scala.onlineshop.ResponseMessages.PRODUCT_NOT_FOUND
import uz.scala.onlineshop.domain.ProductId
import uz.scala.onlineshop.exception.AError
import uz.scala.onlineshop.repos.dto.Product
import uz.scala.onlineshop.repos.sql.ProductsSql
import uz.scala.skunk.syntax.all.skunkSyntaxCommandOps
import uz.scala.skunk.syntax.all.skunkSyntaxQueryOps
import uz.scala.skunk.syntax.all.skunkSyntaxQueryVoidOps

trait ProductsRepository[F[_]] {
  def fetchAll: F[List[Product]]
  def fetchById(id: ProductId): F[Option[Product]]
  def create(product: Product): F[Unit]
  def update(id: ProductId)(update: Product => Product)(implicit lang: Language): F[Unit]
  def delete(id: ProductId): F[Unit]
}

object ProductsRepository {
  def make[F[_]: Async](
      implicit
      session: Resource[F, Session[F]]
    ): ProductsRepository[F] = new ProductsRepository[F] {
    override def fetchAll: F[List[Product]] =
      ProductsSql.fetch.all

    override def fetchById(id: ProductId): F[Option[Product]] =
      ProductsSql.fetchById.queryOption(id)

    override def create(product: Product): F[Unit] =
      ProductsSql.insert.execute(product)

    override def update(
        id: ProductId
      )(
        update: Product => Product
      )(implicit
        lang: Language
      ): F[Unit] =
      OptionT(fetchById(id))
        .semiflatMap { oldProduct =>
          ProductsSql.update.execute(update(oldProduct))
        }
        .getOrRaise(AError.Internal(PRODUCT_NOT_FOUND(lang)))

    override def delete(id: ProductId): F[Unit] =
      ProductsSql.delete.execute(id)
  }
}
