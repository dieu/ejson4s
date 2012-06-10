/**
 * @author apanasenko
 */

import sbt._
import Keys._
import Reference._

object JsonBuild extends Build {
  val JsonVersion = "0.1-SNAPSHOT"

  lazy val buildSettings = Seq(
    organization := "me.apanasenko.json",
    version := JsonVersion,
    scalaVersion := "2.9.1"
  )

  lazy val root = Project(
    id = "simple-json",
    base = file("."),
    settings = defaultSettings ++ Seq(
      libraryDependencies ++= Seq(scalaTest, scalap)
    )
  )

  val scalaTest = "org.scalatest" %% "scalatest" % "1.6.1" % "test"
  val scalap = "org.scala-lang" % "scalap" % "2.9.1"

  lazy val defaultSettings = genericSettings ++ buildSettings

  lazy val genericSettings = Defaults.defaultSettings ++ Seq(
    externalResolvers <<= resolvers map { rs =>
      Resolver.withDefaultResolvers(rs, mavenCentral = true, scalaTools = false)
    },

//    testListeners <<= target.map(t => Seq(new eu.henkelmann.sbt.JUnitXmlTestsListener(t.getAbsolutePath, ConsoleLogger()))),

    // compile options
    scalacOptions ++= Seq("-encoding", "UTF-8", "-deprecation", "-unchecked") ++ (
      if (true || (System getProperty "java.runtime.version" startsWith "1.7")) Seq() else Seq("-optimize")),
    javacOptions ++= Seq("-Xlint:unchecked", "-Xlint:deprecation"),

    ivyLoggingLevel in ThisBuild := UpdateLogging.Quiet
  )
}