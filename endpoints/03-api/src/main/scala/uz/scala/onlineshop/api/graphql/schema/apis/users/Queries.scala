package uz.scala.onlineshop.api.graphql.schema.apis.users

import caliban.schema.Annotations.GQLDescription
import uz.scala.onlineshop.domain.AuthedUser.User
import uz.scala.onlineshop.domain.UserId

case class Queries[F[_]](
    @GQLDescription("Fetch all users")
    users: F[List[User]],
    @GQLDescription("Fetch user by id")
    userById: UserId => F[Option[User]],
  )
