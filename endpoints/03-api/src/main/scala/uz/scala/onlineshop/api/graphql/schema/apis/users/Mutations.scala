package uz.scala.onlineshop.api.graphql.schema.apis.users

import caliban.schema.Annotations.GQLDescription
import uz.scala.onlineshop.domain.UserId
import uz.scala.onlineshop.domain.users.UserInput
import uz.scala.onlineshop.domain.users.UserUpdateInput

case class Mutations[F[_]](
    @GQLDescription("Create User")
    createUser: UserInput => F[UserId],
    @GQLDescription("Update User")
    updateUser: UserUpdateInput => F[Unit],
    @GQLDescription("Delete User")
    deleteUser: UserId => F[Unit],
  )
