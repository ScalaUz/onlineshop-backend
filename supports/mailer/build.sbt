import Dependencies.javax.mailer

name := "mailer"

libraryDependencies ++= Seq(mailer)

dependsOn(LocalProject("common"))
