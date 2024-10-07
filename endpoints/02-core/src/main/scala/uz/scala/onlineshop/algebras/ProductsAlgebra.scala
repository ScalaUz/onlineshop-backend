package uz.scala.onlineshop.algebras

import cats.Monad
import cats.implicits.toFlatMapOps
import cats.implicits.toFunctorOps
import uz.scala.onlineshop.Language
import uz.scala.onlineshop.domain.ProductId
import uz.scala.onlineshop.domain.products.Product
import uz.scala.onlineshop.domain.products.ProductInput
import uz.scala.onlineshop.domain.products.ProductUpdateInput
import uz.scala.onlineshop.effects.Calendar
import uz.scala.onlineshop.effects.GenUUID
import uz.scala.onlineshop.repos.ProductsRepository
import uz.scala.onlineshop.repos.dto
import uz.scala.onlineshop.utils.ID

trait ProductsAlgebra[F[_]] {
  def fetchAll(implicit lang: Language): F[List[Product]]
  def fetchById(id: ProductId)(implicit lang: Language): F[Option[Product]]
  def create(product: ProductInput): F[Unit]
  def update(product: ProductUpdateInput)(implicit lang: Language): F[Unit]
  def delete(id: ProductId): F[Unit]
}
object ProductsAlgebra {
  def make[F[_]: Monad: Calendar: GenUUID](
      productsRepository: ProductsRepository[F]
    ): ProductsAlgebra[F] =
    new ProductsAlgebra[F] {
      override def fetchAll(implicit lang: Language): F[List[Product]] =
        productsRepository.fetchAll.map(_.map(_.toView))

      override def fetchById(id: ProductId)(implicit lang: Language): F[Option[Product]] =
        productsRepository.fetchById(id).map(_.map(_.toView))

      override def create(product: ProductInput): F[Unit] =
        for {
          id <- ID.make[F, ProductId]
          now <- Calendar[F].currentZonedDateTime
          productDto = dto.Product(
            id = id,
            createdAt = now,
            nameUz = product.nameUz,
            nameRu = product.nameRu,
            nameEn = product.nameEn,
            descriptionUz = product.descriptionUz,
            descriptionRu = product.descriptionRu,
            descriptionEn = product.descriptionEn,
            linkCode = product.linkCode,
            price = product.price,
            stock = product.stock,
            brandId = product.brandId,
            categoryId = product.categoryId,
          )
          _ <- productsRepository.create(productDto)
        } yield {}

      override def update(product: ProductUpdateInput)(implicit lang: Language): F[Unit] =
        productsRepository.update(product.productId) { oldProduct =>
          oldProduct.copy(
            nameUz = product.nameUz.getOrElse(oldProduct.nameUz),
            nameRu = product.nameRu.getOrElse(oldProduct.nameRu),
            nameEn = product.nameEn.getOrElse(oldProduct.nameEn),
            descriptionUz = product.descriptionUz.getOrElse(oldProduct.descriptionUz),
            descriptionRu = product.descriptionRu.getOrElse(oldProduct.descriptionRu),
            descriptionEn = product.descriptionEn.getOrElse(oldProduct.descriptionEn),
            linkCode = product.linkCode.getOrElse(oldProduct.linkCode),
            price = product.price.getOrElse(oldProduct.price),
            stock = product.stock.getOrElse(oldProduct.stock),
            brandId = product.brandId.getOrElse(oldProduct.brandId),
          )
        }

      override def delete(id: ProductId): F[Unit] =
        productsRepository.delete(id)
    }
}
