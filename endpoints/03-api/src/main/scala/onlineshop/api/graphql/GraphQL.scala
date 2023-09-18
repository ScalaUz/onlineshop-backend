package onlineshop.api.graphql

import scala.concurrent.ExecutionContext
import scala.util.Failure
import scala.util.Success

import cats.effect._
import cats.implicits._
import io.circe._
import sangria.ast
import sangria.execution._
import sangria.marshalling.circe.CirceResultMarshaller
import sangria.marshalling.circe._
import sangria.parser.QueryParser
import sangria.parser.SyntaxError
import sangria.schema.Schema

import onlineshop.api.graphql.schema.Val

trait GraphQL[F[_]] {
  def query(request: Json): F[Either[Json, Json]]

  def query(
      query: String,
      operationName: Option[String],
      variables: Json,
    ): F[Either[Json, Json]]
}

object GraphQL {
  def apply[F[_]] = new Partial[F]
  final class Partial[F[_]] {
    def apply[A](
        schema: Schema[A, Val],
        userContext: F[A],
      )(implicit
        F: Async[F]
      ): GraphQL[F] =
      new GraphQL[F] {
        private def fail(j: Json): F[Either[Json, Json]] =
          F.pure(j.asLeft)

        def exec(
            schema: Schema[A, Val],
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
                  exceptionHandler = ExceptionHandler {
                    case (_, e) => HandledException(e.getMessage)
                  },
                )
            }))
            result <- execution match {
              case Right(json) => F.pure(json.asRight)
              case Left(err: WithViolations) => fail(GraphQLError(err))
              case Left(err) => fail(GraphQLError.fromThrow(err))
            }
          } yield result

        override def query(request: Json): F[Either[Json, Json]] = {
          val queryString = request.hcursor.downField("query").as[String].toOption
          val operationName = request.hcursor.downField("operationName").as[String].toOption
          val variables =
            Json.fromJsonObject(
              request.hcursor.downField("variables").as[JsonObject].getOrElse(JsonObject.empty)
            )

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
            case Failure(e) => fail(GraphQLError.fromThrow(e))
          }
      }
  }
}
