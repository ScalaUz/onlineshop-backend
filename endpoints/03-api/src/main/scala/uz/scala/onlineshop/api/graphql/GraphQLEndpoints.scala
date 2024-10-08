package uz.scala.onlineshop.api.graphql

import caliban.GraphQL
import caliban.interop.cats.CatsInterop
import caliban.uploads.Uploads
import caliban.wrappers.DeferSupport
import caliban.wrappers.Wrappers._
import cats.effect.Async
import cats.effect.std.Dispatcher
import dev.profunktor.auth.jwt
import uz.scala.onlineshop.Algebras
import uz.scala.onlineshop.Language
import uz.scala.onlineshop.algebras._
import uz.scala.onlineshop.api.graphql.schema.GraphqlApi
import uz.scala.onlineshop.api.graphql.schema.apis.auth.AuthApi
import uz.scala.onlineshop.api.graphql.schema.apis.brands.BrandsApi
import uz.scala.onlineshop.api.graphql.schema.apis.categories.CategoriesApi
import uz.scala.onlineshop.api.graphql.schema.apis.customers.CustomersApi
import uz.scala.onlineshop.api.graphql.schema.apis.products.ProductsApi
import uz.scala.onlineshop.auth.impl.Auth
import uz.scala.onlineshop.domain.AuthedUser
import zio.Runtime
import zio.Unsafe
import zio.ZEnvironment
import zio.durationInt

class GraphQLEndpoints[F[_]: Async](
    algebras: Algebras[F]
  )(implicit
    dispatcher: Dispatcher[F],
    graphQLContext: GraphQLContext,
  ) {
  implicit private val Algebras(
    auth: Auth[F, Option[AuthedUser]],
    users: UsersAlgebra[F],
    assets: AssetsAlgebra[F],
    brands: BrandsAlgebra[F],
    categories: CategoriesAlgebra[F],
    customers: CustomersAlgebra[F],
    products: ProductsAlgebra[F],
  ) = algebras
  implicit val runtime: Runtime[GraphQLContext] =
    Runtime.default.withEnvironment(ZEnvironment(graphQLContext))
  implicit val interop: CatsInterop[F, GraphQLContext] =
    CatsInterop.default[F, GraphQLContext](dispatcher)
  implicit val runtimeUpload: Runtime[Uploads] =
    Unsafe.unsafe { implicit unsafe =>
      Runtime.unsafe.fromLayer(graphQLContext.uploads)
    }
  implicit val interopUploads: CatsInterop[F, Uploads] =
    CatsInterop.default[F, Uploads](dispatcher)

  implicit val lang: Language = graphQLContext.language
  implicit val token: Option[jwt.JwtToken] = graphQLContext.jwtToken
  private val apis: List[GraphqlApi[F, GraphQLContext]] =
    List(
      AuthApi.make(auth),
      ProductsApi.make(products),
      CategoriesApi.make(categories),
      BrandsApi.make(brands),
      CustomersApi.make(customers),
    )

  val createGraphQL: GraphQL[GraphQLContext] =
    apis.map(_.graphql).reduce(_ |+| _) @@
      maxDepth(50) @@
      timeout(3.seconds) @@
      printSlowQueries(500.millis) @@
      authWrapper @@
      DeferSupport.defer @@
      printErrors
}
