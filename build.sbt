scalaVersion := "2.11.0"

organization := "ps.tricerato"

name := "pureimage"

libraryDependencies += "org.specs2" %% "specs2" % "2.3.11" % "test"

libraryDependencies += "commons-io" % "commons-io" % "2.1" % "test"

seq(bintrayPublishSettings:_*)

licenses += ("MIT", url("http://opensource.org/licenses/MIT"))

version := "0.1.2"