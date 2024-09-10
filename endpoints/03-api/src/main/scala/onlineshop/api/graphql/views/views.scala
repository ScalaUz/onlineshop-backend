package onlineshop.api.graphql

import zio.query.ZQuery

package object views {
  type ConsoleQuery[A] = ZQuery[GraphQLContext, Throwable, A]
}
