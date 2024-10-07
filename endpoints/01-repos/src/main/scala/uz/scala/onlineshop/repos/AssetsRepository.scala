package uz.scala.onlineshop.repos

import cats.data.NonEmptyList
import cats.effect.Async
import cats.effect.Resource
import skunk._
import uz.scala.onlineshop.domain.Asset
import uz.scala.onlineshop.domain.AssetId
import uz.scala.onlineshop.repos.sql.AssetsSql
import uz.scala.skunk.syntax.all.skunkSyntaxCommandOps
import uz.scala.skunk.syntax.all.skunkSyntaxQueryOps

trait AssetsRepository[F[_]] {
  def create(asset: Asset): F[Unit]
  def findById(id: AssetId): F[Option[Asset]]
  def findByIds(assetIds: NonEmptyList[AssetId]): F[List[Asset]]
}

object AssetsRepository {
  def make[F[_]: Async](
      implicit
      session: Resource[F, Session[F]]
    ): AssetsRepository[F] = new AssetsRepository[F] {
    override def create(asset: Asset): F[Unit] =
      AssetsSql.insert.execute(asset)

    override def findById(id: AssetId): F[Option[Asset]] =
      AssetsSql.findById.queryOption(id)

    override def findByIds(assetIds: NonEmptyList[AssetId]): F[List[Asset]] = {
      val listIds = assetIds.toList
      AssetsSql.findByIds(listIds).queryList(listIds)
    }
  }
}
