import java.text.SimpleDateFormat
import java.util.Date

import com.typesafe.sbt.packager.archetypes.JavaAppPackaging
import com.typesafe.sbt.packager.universal.UniversalDeployPlugin
import sbt.Defaults._
import sbt.Keys._
import sbt._
import sbtbuildinfo.Plugin.{BuildInfoKey, _}
import sbtrelease.ReleasePlugin._

object ApplicationBuild extends Build {

  lazy val root = Project("thrift", file("."))
    .settings(coreDefaultSettings: _*)
    .settings(libraryDependencies ++= Seq("org.apache.thrift" % "libthrift" % "0.9.2",
    "org.slf4j" % "slf4j-api" % "1.7.12",
    "ch.qos.logback" % "logback-classic" % "1.1.3"))
    .settings(mainClass in Compile := Some("com.colobu.rpctest.AppServer"))
    .enablePlugins(JavaAppPackaging)
}