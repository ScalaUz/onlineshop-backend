package uz.scala.onlineshop.api.graphql

import scala.reflect.ClassTag
import scala.reflect.classTag

import caliban.interop.cats.CatsInterop
import cats.Applicative
import cats.data.NonEmptyList
import cats.implicits.toTraverseOps
import uz.scala.onlineshop.api.graphql.views.ConsoleQuery
import zio.Chunk
import zio.Exit
import zio.Trace
import zio.ZIO
import zio.query.CompletedRequestMap
import zio.query.DataSource
import zio.query.Request
import zio.query.ZQuery

object DataFetcher {
  private case class DeferredRequest[K, V](id: K) extends Request[Throwable, V]

  private def apply[F[_]: Applicative, K, V: ClassTag](
      fetcher: NonEmptyList[K] => F[Map[K, V]]
    )(implicit
      interop: CatsInterop[F, GraphQLContext]
    ): DataSource[GraphQLContext, DeferredRequest[K, V]] =
    new DataSource.Batched[GraphQLContext, DeferredRequest[K, V]] {
      val identifier: String = s"${classTag[V].runtimeClass.getSimpleName}DataSource"
      def run(
          requests: Chunk[DeferredRequest[K, V]]
        )(implicit
          trace: Trace
        ): ZIO[GraphQLContext, Nothing, CompletedRequestMap] =
        interop
          .fromEffect(NonEmptyList.fromList(requests.toList.map(_.id)).traverse(fetcher))
          .exit
          .flatMap(result =>
            result.fold(
              err =>
                requests.foldLeft(CompletedRequestMap.empty) {
                  case (map, req) => map.insert(req, Exit.fail(err))
                },
              _.getOrElse(Map.empty).foldLeft(CompletedRequestMap.empty) {
                case (map, (id, user)) => map.insert(DeferredRequest[K, V](id), Exit.succeed(user))
              },
            )
          )
    }

  def fetch[F[_]: Applicative, K, V: ClassTag](
      id: K,
      fetcher: NonEmptyList[K] => F[Map[K, V]],
    )(implicit
      interop: CatsInterop[F, GraphQLContext]
    ): ConsoleQuery[V] =
    ZQuery.fromRequest(DeferredRequest[K, V](id))(
      DataFetcher[F, K, V](fetcher)
    )
}
