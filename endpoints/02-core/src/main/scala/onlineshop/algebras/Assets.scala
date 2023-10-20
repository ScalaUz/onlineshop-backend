package onlineshop.algebras

import cats.Monad
import cats.implicits.toFlatMapOps
import cats.implicits.toFunctorOps
import cats.implicits.toTraverseOps
import eu.timepit.refined.types.string.NonEmptyString
import org.http4s.multipart.Part
import uz.scala.aws.s3.S3Client
import uz.scala.syntax.refined.commonSyntaxAutoRefineOptV

import onlineshop.domain.Asset
import onlineshop.domain.Asset.AssetInfo
import onlineshop.domain.AssetId
import onlineshop.effects.Calendar
import onlineshop.effects.GenUUID
import onlineshop.repos.AssetsRepository
import onlineshop.utils.ID

trait Assets[F[_]] {
  def create(assetInfo: AssetInfo, fileKey: NonEmptyString): F[AssetId]
  def uploadFile(parts: Vector[Part[F]]): F[Option[NonEmptyString]]
}
object Assets {
  def make[F[_]: Monad: GenUUID: Calendar: Lambda[M[_] => fs2.Compiler[M, M]]](
      assetsRepository: AssetsRepository[F],
      s3Client: S3Client[F],
    ): Assets[F] =
    new Assets[F] {
      override def create(assetInfo: AssetInfo, fileKey: NonEmptyString): F[AssetId] =
        for {
          id <- ID.make[F, AssetId]
          now <- Calendar[F].currentZonedDateTime
          asset = Asset(
            id = id,
            createdAt = now,
            s3Key = fileKey,
            public = assetInfo.public,
            fileName = assetInfo.filename,
          )
          _ <- assetsRepository.create(asset)
        } yield id

      private def getFileType(filename: String): String = filename.dropWhile(_ == '.').tail

      private def genFileKey(orgFilename: String): F[String] =
        GenUUID[F].make.map { uuid =>
          uuid.toString + "." + getFileType(orgFilename)
        }

      private def uploadToS3(filename: String): fs2.Pipe[F, Byte, String] = body =>
        for {
          key <- fs2.Stream.eval(genFileKey(filename))
          _ <- body.through(s3Client.putObject(key))
        } yield key

      override def uploadFile(parts: Vector[Part[F]]): F[Option[NonEmptyString]] = {
        val files = parts.flatMap(p => p.filename.map(_ -> p.body).toVector)
        files
          .traverse {
            case (filename, body) =>
              body.through(uploadToS3(filename))
          }
          .compile
          .toVector
          .map(_.flatten.headOption)
      }
    }
}
