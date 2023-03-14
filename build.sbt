import Dependencies._

ThisBuild / version            := "0.1.0-SNAPSHOT"
ThisBuild / scalaVersion       := "3.2.2"
ThisBuild / evictionErrorLevel := Level.Warn

lazy val root = (project in file("."))
  .settings(
    name := "my-trading-app"
  )
  .aggregate(domain, lib, core, processor)

def dockerSettings(name: String) = List(
  Docker / packageName := s"trading-$name",
  dockerBaseImage      := "jdk17-curl:latest",
  dockerExposedPorts ++= List(8080),
  makeBatScripts     := Nil,
  dockerUpdateLatest := true
)

val commonSettings = List(
  libraryDependencies ++= List(
    CompilerPlugins.zerowaste,
    Libraries.cats,
    Libraries.catsEffect,
    Libraries.circeCore,
    Libraries.circeParser,
    Libraries.cirisCore,
    Libraries.cirisRefined,
    Libraries.fs2Core,
    Libraries.kittens,
    Libraries.ip4sCore,
    Libraries.monocleCore,
    Libraries.ironCore,
    Libraries.ironCats,
    Libraries.ironCirce,
    Libraries.fs2Kafka,
    Libraries.http4sDsl,
    Libraries.http4sMetrics,
    Libraries.http4sServer,
    Libraries.neutronCore,
    Libraries.odin,
    Libraries.redis4catsEffects,
    Libraries.catsLaws         % Test,
    Libraries.monocleLaw       % Test,
    Libraries.scalacheck       % Test,
    Libraries.weaverCats       % Test,
    Libraries.weaverDiscipline % Test,
    Libraries.weaverScalaCheck % Test
  )
)
lazy val domain = (project in file("domain"))
  .settings(commonSettings: _*)

lazy val lib = (project in file("lib"))
  .settings(commonSettings: _*)
  .dependsOn(domain)

lazy val core = (project in file("core"))
  .settings(commonSettings: _*)
  .dependsOn(lib)

lazy val processor = (project in file("processor"))
  .enablePlugins(DockerPlugin)
  .enablePlugins(AshScriptPlugin)
  .settings(commonSettings: _*)
  .settings(dockerSettings("processor"))
  .dependsOn(core, domain)
