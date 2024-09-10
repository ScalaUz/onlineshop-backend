package onlineshop

import cats.MonadThrow
import cats.effect.std.Random
import org.typelevel.log4cats.Logger
import tsec.passwordhashers.PasswordHasher
import tsec.passwordhashers.jca.SCrypt
import uz.scala.aws.s3.S3Client

import onlineshop.algebras._
import onlineshop.auth.impl.Auth
import onlineshop.domain.AuthedUser
import onlineshop.effects.Calendar
import onlineshop.effects.GenUUID

case class Algebras[F[_]](
    auth: Auth[F, Option[AuthedUser]],
    users: UsersAlgebra[F],
    assets: AssetsAlgebra[F],
    brands: BrandsAlgebra[F],
    categories: CategoriesAlgebra[F],
    customers: CustomersAlgebra[F],
    products: ProductsAlgebra[F],
  )

object Algebras {
  def make[F[_]: MonadThrow: Calendar: GenUUID: Logger: Random: Lambda[M[_] => fs2.Compiler[M, M]]](
      auth: Auth[F, Option[AuthedUser]],
      repositories: Repositories[F],
      s3Client: S3Client[F],
    )(implicit
      P: PasswordHasher[F, SCrypt]
    ): Algebras[F] = {
    val Repositories(products, categories, brands, customers, users, assets) = repositories
    val assetsAlgebra = AssetsAlgebra.make[F](assets, s3Client)

    Algebras[F](
      auth = auth,
      users = UsersAlgebra.make[F](users),
      assets = assetsAlgebra,
      brands = BrandsAlgebra.make[F](brands),
      categories = CategoriesAlgebra.make[F](categories),
      customers = CustomersAlgebra.make[F](customers),
      products = ProductsAlgebra.make[F](products),
    )
  }
}
