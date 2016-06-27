import NativePackagerKeys._


name := """vanha"""

version := "1.0"

scalaVersion := "2.11.8"
  
libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.4.7",
  "com.typesafe.akka" %% "akka-slf4j" % "2.4.7",
  "com.typesafe.akka" %% "akka-cluster" % "2.4.7",
  "com.typesafe.akka" %% "akka-testkit" % "2.4.7",
  "ch.qos.logback" % "logback-classic" % "1.1.3"

)
