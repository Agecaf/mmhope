
lazy val root = (project in file(".")).
  settings(
    name := "mmhope",
    organization := "com.agecaf",
    version := "0.0.1",
    scalaVersion := "2.11.8",
    libraryDependencies ++= Seq(
      "com.badlogicgames.gdx" % "gdx" % "1.9.4",
      "com.badlogicgames.gdx" % "gdx-freetype" % "1.9.4",
      "com.badlogicgames.gdx" % "gdx-backend-lwjgl" % "1.9.4",
      "com.badlogicgames.gdx" % "gdx-freetype-platform" % "1.9.4" classifier "natives-desktop",
      "com.badlogicgames.gdx" % "gdx-platform" % "1.9.4" classifier "natives-desktop",
      "org.json4s" %% "json4s-native" % "3.4.0",
      "com.github.pathikrit" %% "better-files" % "2.16.0",
      "org.spire-math" %% "spire" % "0.11.0",
      "org.scala-lang" % "scala-reflect" % scalaVersion.value,
      "org.scala-lang" % "scala-compiler" % scalaVersion.value

    ),
    javacOptions ++= Seq(
      "-Xlint",
      "-encoding", "UTF-8",
      "-source", "1.6",
      "-target", "1.6"
    ),
    scalacOptions ++= Seq(
      "-Xlint",
      "-Ywarn-dead-code",
      "-Ywarn-value-discard",
      // "-Ywarn-numeric-widen", // Gdx prefers Floats, Scala prefers Doubles.
      "-Ywarn-unused",
      "-Ywarn-unused-import",
      "-unchecked",
      "-deprecation",
      "-feature",
      "-encoding", "UTF-8",
      "-target:jvm-1.6"
    ),
    cancelable := true,
    exportJars := true,
    fork in Compile := true
  )