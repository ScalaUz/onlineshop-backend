package uz.scala.onlineshop.algebras

import cats.Monad
import cats.implicits.toFlatMapOps
import cats.implicits.toFunctorOps
import cats.implicits.toTraverseOps
import eu.timepit.refined.types.string.NonEmptyString
import org.http4s.multipart.Part
import uz.scala.aws.s3.S3Client
import uz.scala.onlineshop.domain.Asset
import uz.scala.onlineshop.domain.Asset.AssetInfo
import uz.scala.onlineshop.domain.AssetId
import uz.scala.onlineshop.effects.Calendar
import uz.scala.onlineshop.effects.GenUUID
import uz.scala.onlineshop.repos.AssetsRepository
import uz.scala.onlineshop.utils.ID
import uz.scala.syntax.refined.commonSyntaxAutoRefineOptV

trait AssetsAlgebra[F[_]] {
  def create(assetInfo: AssetInfo, fileKey: NonEmptyString): F[AssetId]
  def uploadFile(parts: Vector[Part[F]]): F[Option[NonEmptyString]]
}
object AssetsAlgebra {
  def make[F[_]: Monad: GenUUID: Calendar: Lambda[M[_] => fs2.Compiler[M, M]]](
      assetsRepository: AssetsRepository[F],
      s3Client: S3Client[F],
    ): AssetsAlgebra[F] =
    new AssetsAlgebra[F] {
      override def create(assetInfo: AssetInfo, fileKey: NonEmptyString): F[AssetId] =
        for {
          id <- ID.make[F, AssetId]
          now <- Calendar[F].currentZonedDateTime
          asset = Asset(
            id = id,
            createdAt = now,
            s3Key = fileKey,
            fileName = assetInfo.fileName,
            mediaType = assetInfo.mediaType,
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
