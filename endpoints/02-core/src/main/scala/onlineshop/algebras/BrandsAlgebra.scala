package onlineshop.algebras

import cats.Monad
import cats.implicits.toFlatMapOps
import cats.implicits.toFunctorOps

import onlineshop.domain.Brand
import onlineshop.domain.BrandId
import onlineshop.domain.args.BrandInput
import onlineshop.effects.GenUUID
import onlineshop.repos.BrandsRepository
import onlineshop.utils.ID
trait BrandsAlgebra[F[_]] {
  def create(brandInput: BrandInput): F[BrandId]
  def fetchAll: F[List[Brand]]
}
object BrandsAlgebra {
  def make[F[_]: Monad: GenUUID](brandsRepository: BrandsRepository[F]): BrandsAlgebra[F] =
    new BrandsAlgebra[F] {
      override def create(brandInput: BrandInput): F[BrandId] =
        for {
          brandId <- ID.make[F, BrandId]
          brand = Brand(
            id = brandId,
            name = brandInput.name,
            assetId = brandInput.assetId,
          )
          _ <- brandsRepository.create(brand)
        } yield brandId

      override def fetchAll: F[List[Brand]] = brandsRepository.fetchAll
    }
}
