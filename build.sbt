import org.scalajs.sbtplugin.ScalaJSPlugin
import org.scalajs.sbtplugin.ScalaJSPlugin.autoImport._

lazy val laminarV      = "0.14.2"
lazy val airstreamV    = "0.14.2"
lazy val lamiNext      = "0.14.2"
lazy val scalajsDomV   = "2.0.0"
lazy val scala3Version = "3.1.1"

lazy val model = project
  .in(file("./model"))
  .settings(
    name         := "todos-web",
    version      := "0.1.0-SNAPSHOT",
    scalaVersion := scala3Version,
  )

lazy val ui = project
  .in(file("ui"))
  .enablePlugins(ScalaJSPlugin)
  .settings(
    name         := "todos-ui",
    version      := "0.1.0-SNAPSHOT",
    scalaVersion := scala3Version,
    scalaJSLinkerConfig ~= {
      _.withModuleKind(ModuleKind.ESModule)
    }, 
    scalaJSUseMainModuleInitializer := true,
    Compile / fastOptJS / artifactPath := baseDirectory.value / "ui" / "js" / "app.js",
    libraryDependencies ++= Seq(
      "com.raquo"    %%% "laminar"     % laminarV,
      "com.raquo"    %%% "airstream"   % airstreamV,
      "com.lihaoyi"  %%% "sourcecode"  % "0.2.8",
      "com.lihaoyi"  %%% "upickle"     % "1.5.0",
      "org.scala-js" %%% "scalajs-dom" % scalajsDomV,
      "io.laminext"  %%% "websocket"   % lamiNext,
      "io.laminext"  %%% "ui"          % lamiNext,
      "io.laminext"  %%% "tailwind"    % lamiNext,
    )
  )
  .dependsOn(model)

val ZioVersion        = "2.0.0-RC1"
val ZioJsonVersion    = "0.3.0-RC2"
val ZioHttpVersion    = "2.0.0-RC2"
val ZioConfigVersion  = "3.0.0-RC1"
val ZioSchemaVersion  = "0.2.0-RC1-1"
val ZioLoggingVersion = "2.0.0-RC4"

lazy val web = project
  .in(file("web"))
  .settings(
    name         := "todos-web",
    version      := "0.1.0-SNAPSHOT",
    scalaVersion := scala3Version,
    libraryDependencies ++= Seq(
    "dev.zio" %% "zio"                   % ZioVersion,
    "dev.zio" %% "zio-json"              % ZioJsonVersion,
    "io.d11"  %% "zhttp"                 % ZioHttpVersion,
    "dev.zio" %% "zio-config"            % ZioConfigVersion,
    "dev.zio" %% "zio-logging"           % ZioLoggingVersion,
    "io.d11"  %% "zhttp-test"            % ZioHttpVersion % Test,
    "dev.zio" %% "zio-test"              % ZioVersion     % Test,
    "dev.zio" %% "zio-test-sbt"          % ZioVersion     % Test
    )
  )
  .dependsOn(model)

lazy val root = project
  .in(file("."))
  .aggregate(model, web, ui)

