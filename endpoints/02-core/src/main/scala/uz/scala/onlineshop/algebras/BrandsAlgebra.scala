package uz.scala.onlineshop.algebras

import cats.Monad
import cats.implicits.toFlatMapOps
import cats.implicits.toFunctorOps
import uz.scala.onlineshop.Language
import uz.scala.onlineshop.domain.BrandId
import uz.scala.onlineshop.domain.brands.Brand
import uz.scala.onlineshop.domain.brands.BrandInput
import uz.scala.onlineshop.domain.brands.BrandUpdateInput
import uz.scala.onlineshop.effects.GenUUID
import uz.scala.onlineshop.repos.BrandsRepository
import uz.scala.onlineshop.repos.dto
import uz.scala.onlineshop.utils.ID

trait BrandsAlgebra[F[_]] {
  def create(brandInput: BrandInput): F[BrandId]
  def fetchAll: F[List[Brand]]
  def findById(id: BrandId): F[Option[Brand]]
  def update(updateInput: BrandUpdateInput)(implicit lang: Language): F[Unit]
  def delete(id: BrandId): F[Unit]
}
object BrandsAlgebra {
  def make[F[_]: Monad: GenUUID](brandsRepository: BrandsRepository[F]): BrandsAlgebra[F] =
    new BrandsAlgebra[F] {
      override def create(brandInput: BrandInput): F[BrandId] =
        for {
          brandId <- ID.make[F, BrandId]
          brand = dto.Brand(
            id = brandId,
            name = brandInput.name,
            assetId = brandInput.assetId,
          )
          _ <- brandsRepository.create(brand)
        } yield brandId

      override def fetchAll: F[List[Brand]] = brandsRepository.fetchAll.map(_.map(_.toView))

      override def findById(id: BrandId): F[Option[Brand]] =
        brandsRepository.fetchById(id).map(_.map(_.toView))

      override def update(updateInput: BrandUpdateInput)(implicit lang: Language): F[Unit] =
        brandsRepository.update(updateInput.id) { oldBrand =>
          oldBrand.copy(
            name = updateInput.name.getOrElse(oldBrand.name),
            assetId = updateInput.assetId.getOrElse(oldBrand.assetId),
          )
        }

      override def delete(id: BrandId): F[Unit] =
        brandsRepository.delete(id)
    }
}
