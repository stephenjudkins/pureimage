package ps.tricerato.pureimage.test

import org.specs2.mutable._
import ps.tricerato.pureimage._

class HelloWorldSpec extends Specification {
  "basic image loading" in {
    val Right(RGBImage(image)) = Input(zardozJpeg)
    (image.width, image.height) must_== (327,327)
  }
}