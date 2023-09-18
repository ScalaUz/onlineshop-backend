package onlineshop.api.graphql

import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.UUID

import sangria.ast
import sangria.macros.derive.deriveObjectType
import sangria.schema.ListType
import sangria.schema.ObjectType
import sangria.schema.ScalarType
import sangria.validation.ValueCoercionViolation

import onlineshop.Repositories
import onlineshop.domain.Category
import onlineshop.domain.Product

package object schema {
  type Ctx[F[_]] = Repositories[F]
  type Val = Any
  private case object UUIDCoercionViolation extends ValueCoercionViolation("Invalid UUID format")

  private case object DateTimeCoercionViolation
      extends ValueCoercionViolation(
        "Invalid ZonedDateTime value"
      )

  implicit val UuidType: ScalarType[UUID] = ScalarType[UUID](
    "UUID",
    coerceOutput = (value, _) => value.toString,
    coerceUserInput = {
      case s: String =>
        try
          Right(UUID.fromString(s))
        catch {
          case _: IllegalArgumentException => Left(UUIDCoercionViolation)
        }
      case _ => Left(UUIDCoercionViolation)
    },
    coerceInput = {
      case s: ast.StringValue =>
        try
          Right(UUID.fromString(s.value))
        catch {
          case _: IllegalArgumentException => Left(UUIDCoercionViolation)
        }
      case _ => Left(UUIDCoercionViolation)
    },
  )
  implicit val ZonedDateTimeType: ScalarType[ZonedDateTime] = ScalarType[ZonedDateTime](
    "ZonedDateTime",
    coerceOutput = (date, _) => DateTimeFormatter.ISO_INSTANT.format(date),
    coerceInput = (input: Any) =>
      input match {
        case s: String =>
          try
            Right(ZonedDateTime.parse(s))
          catch {
            case _: DateTimeParseException => Left(DateTimeCoercionViolation)
          }
        case _ => Left(DateTimeCoercionViolation)
      },
    coerceUserInput = (input: Any) =>
      input match {
        case s: String =>
          try
            Right(ZonedDateTime.parse(s))
          catch {
            case _: DateTimeParseException => Left(DateTimeCoercionViolation)
          }
        case _ => Left(DateTimeCoercionViolation)
      },
  )

  implicit val ProductType: ObjectType[Unit, Product] = deriveObjectType[Unit, Product]()
  implicit val ProductListType: ListType[Product] = ListType(ProductType)

  implicit val CategoryType: ObjectType[Unit, Category] = deriveObjectType[Unit, Category]()
  implicit val CategoryListType: ListType[Category] = ListType(CategoryType)
}
