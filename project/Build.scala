import sbt._
import Keys._

object ProjectBuild extends Build {
    override lazy val settings = super.settings ++ Seq(
        organization := "org.continuumio",
        version := "0.1-SNAPSHOT",
        description := "Example showing how configure a project to use bokeh-scala",
        homepage := Some(url("http://bokeh.pydata.org")),
        licenses := Seq("MIT-style" -> url("http://www.opensource.org/licenses/mit-license.php")),
        scalaVersion := "2.10.3",
        scalacOptions ++= Seq("-Xlint", "-deprecation", "-unchecked", "-feature"),
        shellPrompt := { state =>
            "continuum (%s)> ".format(Project.extract(state).currentProject.id)
        },
        cancelable := true,
        resolvers ++= Seq(
            Resolver.sonatypeRepo("releases"),
            Resolver.sonatypeRepo("snapshots"))
    )

    object Dependencies {
        val breeze = "org.scalanlp" %% "breeze" % "0.6"
        val specs2 = "org.specs2" %% "specs2" % "2.3.8" % "test"
    }

    lazy val exampleSettings = Project.defaultSettings ++ Seq(
        libraryDependencies ++= { import Dependencies._; Seq(breeze, specs2) }
    )

    lazy val example = Project(id="example", base=file("."), settings=exampleSettings)

    override def projects = Seq(example)
}
