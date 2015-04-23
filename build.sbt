lazy val urielstresstest = (project in file("."))
    .settings(
        name := "judgels-uriel-stress-test",
        version := "0.1.0",
        scalaVersion := "2.11.1",
        autoScalaLibrary := false,
        crossPaths := false,
        mainClass in (Compile, run) := Some("org.iatoki.judgels.uriel.stresstest.Main"),
        libraryDependencies ++= Seq(
            "org.apache.httpcomponents" % "httpclient" % "4.4.1",
            "org.apache.httpcomponents" % "httpmime" % "4.4.1",
            "com.google.guava" % "guava" % "r05",
            "org.slf4j" % "slf4j-api" % "1.7.10",
            "commons-io" % "commons-io" % "2.4",
            "ch.qos.logback" % "logback-classic" % "1.1.1",
            "ch.qos.logback" % "logback-core" % "1.1.1"
        )
    )
