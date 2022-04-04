libraryDependencies += "org.scala-js" %% "scalajs-env-jsdom-nodejs" % "1.1.0"
libraryDependencies += "org.scala-js" %% "scalajs-env-selenium" % "1.1.1"
addSbtPlugin("org.scala-js" % "sbt-scalajs" % "1.8.0")
addSbtPlugin("ch.epfl.scala" % "sbt-scalajs-bundler" % "0.21.0-RC1")

// For Scala.js 1.x

// resolvers += Resolver.bintrayRepo("oyvindberg", "converter")
// Current, for Scala.js 1.x.x
//addSbtPlugin("org.scalablytyped.converter" % "sbt-converter" % "1.0.0-beta36")
//addSbtPlugin("org.scalablytyped.converter" % "sbt-converter" % "1.0.0-beta29.2")
