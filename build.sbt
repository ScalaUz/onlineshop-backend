ThisBuild / version := "0.1.0"

ThisBuild / scalaVersion := "2.13.11"

lazy val `online-shop` =
  project
    .in(file("."))
    .settings(
      name := "online-shop"
    )
    .aggregate(endpoints)

lazy val endpoints = project
  .in(file("endpoints"))
  .settings(
    name := "endpoints"
  )
addCommandAlias(
  "styleCheck",
  "all scalafmtSbtCheck; scalafmtCheckAll; Test / compile; scalafixAll --check",
)

Global / lintUnusedKeysOnLoad := false
Global / onChangedBuildSource := ReloadOnSourceChanges
