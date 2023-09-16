package chatassist.endpoint.setup

import chatassist.support.database.MigrationsConfig
import uz.scala.http4s.HttpServerConfig
import uz.scala.skunk.DataBaseConfig

case class Config(
    http: HttpServerConfig,
    database: DataBaseConfig,
  ) {
  lazy val migrations: MigrationsConfig = MigrationsConfig(
    hostname = database.host.value,
    port = database.port.value,
    database = database.database.value,
    username = database.user.value,
    password = database.password.value,
    schema = "public",
    location = "db/migration",
  )
}
