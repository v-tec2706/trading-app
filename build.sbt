import Dependencies._

ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.2.2"

lazy val root = (project in file("."))
  .settings(
    name := "my-trading-app"
  )

libraryDependencies ++= Seq(
  Libraries.cats,
  Libraries.circeCore,
  Libraries.circeParser,
  Libraries.circeRefined,
  Libraries.ip4sCore
)
