package ps.tricerato.pureimage.test

import org.specs2.mutable._
import ps.tricerato.pureimage._

/**
 * Created by michael.schiff on 6/12/14.
 */
class Output extends Specification {
  "RBG image output" in {
    val Right(RGBImage(image)) = Input(zardozJpeg)
    Output(image, PNG).length must_!= 0
  }

  "Gray image output" in {
    val Right(RGBImage(image)) = Input(zardozJpeg)
    val gray = new Image[Gray] {
      def width = image.width
      def height = image.height
      def apply(x:Int, y:Int):Gray = Gray((image(x,y).red + image(x,y).green + image(x,y).blue) / 3)
    }
    Output(gray, PNG).length must_!= 0
  }


}
