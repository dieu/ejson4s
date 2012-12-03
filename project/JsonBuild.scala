/**
 * @author apanasenko
 */

import sbt._
import Keys._

object JsonBuild extends Build {
  import Utils._

  lazy val root = project(".")
    .libraryDependencies(Dependencies.scalap)

  lazy val buildSettings = Seq(
    organization := "me.apanasenko.ejson4s",
    version := "0.1-SNAPSHOT",
    scalaVersion := "2.9.2",
    crossScalaVersions := Seq("2.9.1", "2.9.2"),

    scalacOptions := Seq("-deprecation", "-unchecked", "-explaintypes"),

    libraryDependencies ++= Dependencies.test,

    publishMavenStyle := true,
    publishArtifact in Test := false,
//    publishTo <<= version { (v: String) =>
//      val nexus = "https://oss.sonatype.org/"
//      if (v.trim.endsWith("SNAPSHOT"))
//        Some("snapshots" at nexus + "content/repositories/snapshots")
//      else
//        Some("releases"  at nexus + "service/local/staging/deploy/maven2")
//    },
    licenses := Seq("MIT" -> url("http://opensource.org/licenses/MIT")),
    homepage := Some(url("https://github.com/dieu/ejson4s"))
  )

  def commonSettings = Defaults.defaultSettings ++ buildSettings

  object Dependencies {
    val scalap = scalaVersion("org.scala-lang" % "scalap" % _)

    val test = Seq(
      "org.scalatest" %% "scalatest" % Versions.scalaTest % "test"
    )
  }

  object Versions {
    val scalaTest = "1.6.1"
  }


  object Utils {
    implicit def richProject(p: Project) = new {
      def libraryDependencies(d: Seq[ModuleID]): Project = p.settings(Keys.libraryDependencies ++= d)

      def libraryDependencies(d: sbt.Project.Initialize[ModuleID]): Project = p.settings(Keys.libraryDependencies <+= d)
    }

    def project(path: String) = Project(
      id = if (path == ".") "ejson4s" else path.replace('/', '-'),
      base = file(path),
      settings = commonSettings)
  }
}
