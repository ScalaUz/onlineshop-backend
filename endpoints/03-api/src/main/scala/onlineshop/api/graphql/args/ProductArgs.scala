package onlineshop.api.graphql.args

case class ProductArgs(
    name: Option[String]
  )
object ProductArgs {
  import caliban.schema._
  implicit val productArgsBuilder: ArgBuilder[ProductArgs] = ArgBuilder.gen
}
