package uz.scala.onlineshop

import cats.effect.Sync
import cats.effect.std.Random
import org.typelevel.log4cats.Logger
import tsec.passwordhashers.PasswordHasher
import tsec.passwordhashers.jca.SCrypt
import uz.scala.aws.s3.S3Client
import uz.scala.onlineshop.algebras._
import uz.scala.onlineshop.auth.AuthConfig
import uz.scala.onlineshop.auth.impl.Auth
import uz.scala.onlineshop.domain.AuthedUser
import uz.scala.redis.RedisClient

case class Algebras[F[_]](
    auth: Auth[F, Option[AuthedUser]],
    users: UsersAlgebra[F],
    assets: AssetsAlgebra[F],
    brands: BrandsAlgebra[F],
    categories: CategoriesAlgebra[F],
    customers: CustomersAlgebra[F],
    products: ProductsAlgebra[F],
    addresses: AddressesAlgebra[F],
  )

object Algebras {
  def make[F[_]: Sync: Logger: Random: Lambda[M[_] => fs2.Compiler[M, M]]](
      config: AuthConfig,
      repositories: Repositories[F],
      s3Client: S3Client[F],
      redis: RedisClient[F],
    )(implicit
      P: PasswordHasher[F, SCrypt]
    ): Algebras[F] = {
    val customers = CustomersAlgebra.make[F](repositories.customers)
    val users = UsersAlgebra.make[F](repositories.users)
    val assetsAlgebra = AssetsAlgebra.make[F](repositories.assets, s3Client)

    Algebras[F](
      auth = Auth.make[F](config, users, customers, redis),
      users = UsersAlgebra.make[F](repositories.users),
      assets = assetsAlgebra,
      brands = BrandsAlgebra.make[F](repositories.brands),
      categories = CategoriesAlgebra.make[F](repositories.categories),
      customers = CustomersAlgebra.make[F](repositories.customers),
      products = ProductsAlgebra.make[F](repositories.products),
      addresses = AddressesAlgebra.make[F](repositories.addresses),
    )
  }
}
