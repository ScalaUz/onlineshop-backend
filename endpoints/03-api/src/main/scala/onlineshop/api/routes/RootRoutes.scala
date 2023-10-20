package onlineshop.api.routes

import caliban.GraphQLRequest
import cats.data.OptionT
import cats.effect.Async
import cats.implicits.toFlatMapOps
import cats.implicits.toFunctorOps
import org.http4s.AuthedRoutes
import org.http4s._
import org.http4s.circe.CirceEntityCodec._
import org.http4s.multipart.Multipart
import uz.scala.http4s.syntax.all.http4SyntaxPartOps
import uz.scala.http4s.utils.Routes

import onlineshop.algebras.Assets
import onlineshop.api.graphql.GraphQLEndpoints
import onlineshop.domain.Asset.AssetInfo
import onlineshop.domain.AuthedUser

final case class RootRoutes[F[_]: Async](
    graphQL: Option[AuthedUser] => GraphQLEndpoints[F],
    assets: Assets[F],
  ) extends Routes[F, Option[AuthedUser]] {
  override val path: String = "/"
  override val public: HttpRoutes[F] = HttpRoutes.empty
  override val `private`: AuthedRoutes[Option[AuthedUser], F] =
    AuthedRoutes.of[Option[AuthedUser], F] {
      case ar @ POST -> Root / "asset" as _ =>
        ar.req.decode[Multipart[F]] { multipart =>
          OptionT(assets.uploadFile(multipart.parts.fileParts))
            .foldF(BadRequest("File part not exists!")) { fileKey =>
              for {
                assetInfo <- multipart.parts.textParts.convert[AssetInfo]
                assetId = assets.create(assetInfo, fileKey)
                result <- Created(assetId)
              } yield result
            }
        }

      case ar @ POST -> Root / "graphql" as authedUser =>
        for {
          interpreter <- graphQL(authedUser)
            .interop
            .toEffect(graphQL(authedUser).createGraphQL.interpreter)
          query <- ar.req.as[GraphQLRequest]
          result <- graphQL(authedUser).interop.toEffect(interpreter.executeRequest(query))
          response <- Ok(result)
        } yield response
    }
}
