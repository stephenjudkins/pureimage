# PureImage

PureImage is a raster image processing library written in Scala. See slides at [http://stephenjudkins.github.io/pureimage-presentation/]

## Motivation

Available image processing and manipulation libraries are difficult to use. They feature byzantive and opaque APIs. [http://docs.oracle.com/javase/7/docs/api/java/awt/image/BufferedImage.html] Or, they require concrete in-memory representations of pixel data in order to perform manipulations. [http://www.imagemagick.org/script/magick-core.php]

However, images are simple things. We aim to provide a set of tools that operate on a single simple abstraction.

## API

```
trait Image[A] {
  def width: Int
  def height: Int
  def apply(x: Int, y: Int):A
}
```

Further, we aim to support image input in a similarly simple manner:
```
sealed trait LoadedImage
case class RGBImage(image: Image[RGB]) extends LoadedImage
case class RGBAImage(image: Image[RGBA]) extends LoadedImage
case class GrayImage(image: Image[Gray]) extends LoadedImage

sealed trait ReadError
case object UnsupportedImageType extends ReadError
case class ReadException(e: Exception) extends ReadError

object Input {
  def apply(data: Array[Byte]):Either[ReadError, LoadedImage] = ???
}
```

Output, which may require us to deal with arbitrary output formats and pixel types, sees utility from applying type classes [http://en.wikipedia.org/wiki/Type_class ]:
```
trait Output[I, O <: OutputFormat] {
  def apply(i: Image[I], o: O):Array[Byte]
}

object Output {
  def apply[I, O <: OutputFormat](image: Image[I], format: O)(implicit output: Output[I,O]) = ???
}
```

Likewise, we support common operations on pixel types, both included and user-defined, using type classes:
```
trait Pixel[A] {
  def sum(a: A, b: A):A
  def fade(p: A, f: Float):A
  def zero: A
}
```

Included pixel types in include RGB, RGBA, and grayscale. All are represented internally as 32-bit integers. Once certain Scala issues are addressed [https://issues.scala-lang.org/browse/SI-5611] specialization should give us fast performance. Currently these representations are boxed at runtime, but performance should be reasonably good for many use cases.

## Getting PureImage
Add the following to your `build.sbt`:
```
resolvers += "stephenjudkins-bintray" at "http://dl.bintray.com/stephenjudkins/maven"

libraryDependencies += "ps.tricerato" %% "pureimage" % "0.1.2"
```

