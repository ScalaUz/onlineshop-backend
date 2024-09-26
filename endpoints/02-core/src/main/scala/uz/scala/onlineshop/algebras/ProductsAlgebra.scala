package uz.scala.onlineshop.algebras

import cats.Monad
import cats.implicits.toFunctorOps
import uz.scala.onlineshop.Language
import uz.scala.onlineshop.domain.products.Product
import uz.scala.onlineshop.repos.ProductsRepository
trait ProductsAlgebra[F[_]] {
  def fetchAll(implicit lang: Language): F[List[Product]]
}
object ProductsAlgebra {
  def make[F[_]: Monad](productsRepository: ProductsRepository[F]): ProductsAlgebra[F] =
    new ProductsAlgebra[F] {
      override def fetchAll(implicit lang: Language): F[List[Product]] =
        productsRepository.fetchAll.map(_.map(_.toView))
    }
}
