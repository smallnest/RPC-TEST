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

lazy val root = Project("gRPC", file("."))
	.settings(coreDefaultSettings: _*)
	.settings(libraryDependencies ++= Seq("io.grpc" % "grpc-all" % "0.7.1"))
	.settings(mainClass in Compile := Some("com.colobu.rpctest.AppServer"))
	.enablePlugins(JavaAppPackaging)
}