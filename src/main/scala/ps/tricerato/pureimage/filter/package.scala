package ps.tricerato.pureimage

package object filter {

  object Rotate {
    sealed trait Amount
    case object Clockwise90 extends Amount
    case object Clockwise180 extends Amount
    case object Clockwise270 extends Amount

    def apply[A : Pixel](a: Amount, image: Image[A]):Image[A] = {
      val f = (i: Image[A]) => apply[A](Clockwise90, i)
      a match {
        case Clockwise90 => new Image[A] {
          val width = image.height; def height = image.width
          def apply(x: Int, y: Int) = image(y, image.height - x - 1)
        }
        case Clockwise180 => image |> f |> f
        case Clockwise270 => image |> f |> f |> f
      }
    }
  }

  def squareCrop[A : Pixel](image: Image[A]) = {
    import math._
    if (image.width > image.height) {
      val diff = (image.width - image.height) / 2
      new Image[A] {
        val width = image.height; val height = image.height
        def apply(x: Int, y: Int) = image(diff + x, y)
      }
    } else {
      val diff = (image.height - image.width) / 2

      new Image[A] {
        val width = image.width; val height = image.width
        def apply(x: Int, y: Int) = image(x, y + diff)
      }

    }

  }

  def scale[A : Pixel](factor: Float, image: Image[A]) = {
    new Image[A] {
      val width = (image.width * factor).toInt
      val height = (image.height * factor).toInt
      def apply(x: Int, y: Int) = image((x * factor).toInt, (y * factor).toInt)
    }
  }

}