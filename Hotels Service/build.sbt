lazy val akkaHttpVersion = "10.1.8"
lazy val akkaVersion    = "2.5.21"

enablePlugins(JavaAppPackaging)

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization    := "com.example",
      scalaVersion    := "2.12.7",
      mainClass in Compile := Some("QuickstartServer"),
      version := "1.0"
    )),
    name := "Hotels",
    resolvers += "typesafe" at "http://repo.typesafe.com/typesafe/releases/",
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-http"            % akkaHttpVersion,
      "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion,
      "com.typesafe.akka" %% "akka-http-xml"        % akkaHttpVersion,
      "com.typesafe.akka" %% "akka-stream"          % akkaVersion,

      "com.typesafe.akka" %% "akka-http-testkit"    % akkaHttpVersion % Test,
      "com.typesafe.akka" %% "akka-testkit"         % akkaVersion     % Test,
      "com.typesafe.akka" %% "akka-stream-testkit"  % akkaVersion     % Test,
      "org.scalatest"     %% "scalatest"            % "3.0.5"         % Test,

      "com.typesafe.slick" %% "slick" % "3.3.0",
      "com.typesafe.slick"  %% "slick-hikaricp" % "3.3.0",
      "org.slf4j" % "slf4j-nop" % "1.6.4",
      "com.typesafe.slick" %% "slick-hikaricp" % "3.3.0",
      "org.xerial"          %  "sqlite-jdbc"          % "3.7.2"
    )
  )
