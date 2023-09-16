package onlineshop.api.graphql

import scala.concurrent.ExecutionContext
import scala.util.Failure
import scala.util.Success

import cats.effect._
import cats.implicits._
import io.circe._
import io.circe.optics.JsonPath._
import sangria.ast
import sangria.execution._
import sangria.marshalling.InputUnmarshaller
import sangria.marshalling.circe.CirceResultMarshaller
import sangria.marshalling.circe._
import sangria.parser.QueryParser
import sangria.parser.SyntaxError
import sangria.schema.Schema

trait GraphQL[F[_]] {
  def query(request: Json): F[Either[Json, Json]]

  def query(
      query: String,
      operationName: Option[String],
      variables: Json,
    ): F[Either[Json, Json]]
}

object GraphQL {
  private val queryStringLens = root.query.string
  private val operationNameLens = root.operationName.string
  private val variablesLens = root.variables.obj

  def apply[F[_]] = new Partial[F]
  final class Partial[F[_]] {
    def apply[A](
        schema: Schema[A, Unit],
        userContext: F[A],
      )(implicit
        F: Async[F],
        um: InputUnmarshaller[Json],
      ): GraphQL[F] =
      new GraphQL[F] {
        private def fail(j: Json): F[Either[Json, Json]] =
          F.pure(j.asLeft)

        def exec(
            schema: Schema[A, Unit],
            userContext: F[A],
            query: ast.Document,
            operationName: Option[String],
            variables: Json,
          ): F[Either[Json, Json]] =
          for {
            ctx <- userContext
            implicit0(executionContext: ExecutionContext) <- Async[F].executionContext
            execution <- F.attempt(F.fromFuture(F.delay {
              Executor
                .execute(
                  schema = schema,
                  queryAst = query,
                  userContext = ctx,
                  variables = variables,
                  operationName = operationName,
                  middleware = Nil,
                )
            }))
            result <- execution match {
              case Right(json) => F.pure(json.asRight)
              case Left(err: WithViolations) => fail(GraphQLError(err))
              case Left(err) => F.raiseError(err)
            }
          } yield result

        override def query(request: Json): F[Either[Json, Json]] = {
          val queryString = queryStringLens.getOption(request)
          val operationName = operationNameLens.getOption(request)
          val variables =
            Json.fromJsonObject(variablesLens.getOption(request).getOrElse(JsonObject()))

          queryString match {
            case Some(qs) => query(qs, operationName, variables)
            case None => fail(GraphQLError("No 'query' property was present in the request."))
          }
        }

        override def query(
            query: String,
            operationName: Option[String],
            variables: Json,
          ): F[Either[Json, Json]] =
          QueryParser.parse(query) match {
            case Success(ast) => exec(schema, userContext, ast, operationName, variables)
            case Failure(e: SyntaxError) => fail(GraphQLError(e))
            case Failure(e) => F.raiseError(e)
          }
      }
  }
}
