package onlineshop.repos

import cats.effect.Async
import cats.effect.Resource
import skunk._
import uz.scala.skunk.syntax.all.skunkSyntaxCommandOps

import onlineshop.domain.Asset
import onlineshop.repos.sql.AssetsSql

trait AssetsRepository[F[_]] {
  def create(asset: Asset): F[Unit]
}

object AssetsRepository {
  def make[F[_]: Async](
      implicit
      session: Resource[F, Session[F]]
    ): AssetsRepository[F] = new AssetsRepository[F] {
    override def create(asset: Asset): F[Unit] =
      AssetsSql.insert.execute(asset)
  }
}
