scalaVersion := "2.10.2"

organization := "ps.tricerato"

name := "pureimage"

libraryDependencies += "org.specs2" %% "specs2" % "2.2.3" % "test"

libraryDependencies += "commons-io" % "commons-io" % "2.1" % "test"

seq(bintrayPublishSettings:_*)

licenses += ("MIT", url("http://opensource.org/licenses/MIT"))

version := "0.1.0"