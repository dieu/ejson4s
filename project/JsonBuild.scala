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
    organization := "me.apanasenko.json",
    version := "0.1",
    scalaVersion := "2.9.2",

    scalacOptions := Seq("-deprecation", "-unchecked", "-explaintypes"),

    libraryDependencies ++= Dependencies.test
  )

  def commonSettings = Defaults.defaultSettings ++ buildSettings

  object Dependencies {
    val scalap = Seq(
      "org.scala-lang" % "scalap" % Versions.scalap
    )

    val test = Seq(
      "org.scalatest" %% "scalatest" % Versions.scalaTest % "test"
    )
  }

  object Versions {
    val scalap = "2.9.2"
    val scalaTest = "1.6.1"
  }


  object Utils {
    implicit def richProject(p: Project) = new {
      def libraryDependencies(d: Seq[ModuleID]): Project = p.settings(Keys.libraryDependencies ++= d)
    }

    def project(path: String) = Project(
      id = if (path == ".") "root" else path.replace('/', '-'),
      base = file(path),
      settings = commonSettings)
  }
}
