package ps.tricerato.pureimage

case class RGB(i: Int) extends AnyVal {
  def red = (i & 0xff)
  def green = ((i >> 8) & 0xff)
  def blue = ((i >> 16) & 0xff)
}
object RGB {
  def fromChannels(red: Int, green: Int, blue: Int) = RGB(
    0 | red | ( green << 8 ) | ( blue << 16 )
  )
}
case class RGBA(i: Int) extends AnyVal {
  def red = (i & 0xff).toByte
  def green = ((i >> 8) & 0xff).toByte
  def blue = ((i >> 16) & 0xff).toByte
  def alpha = ((i >> 24) & 0xff).toByte
}
case class Gray(i: Int) extends AnyVal {
  def white = (i & 0xff).toByte
}

trait Pixel[@specialized(Int) A] {
  def sum(a: A, b: A):A
  def fade(p: A, f: Float):A
  def zero: A
}

object Pixel {
  def get[A : Pixel] = implicitly[Pixel[A]]

  implicit object RGBPixel extends Pixel[RGB] {
    def sum(a: RGB, b: RGB) = RGB.fromChannels(
      between(0, a.red + b.red, 255),
      between(0, a.green + b.green, 255),
      between(0, a.blue + b.blue, 255)
    )

    def fade(p: RGB, f: Float) = RGB.fromChannels(
      between(0, (p.red * f).toInt, 255),
      between(0, (p.green * f).toInt, 255),
      between(0, (p.blue * f).toInt, 255)
      // (p.green * f).toInt,
      // (p.blue * f).toInt
    )

    val zero = RGB(0)

    // def build(width: Int, height: Int, f: (Int, Int) => RGB) = RGBImage(width, height, f)
  }
}

trait Image[@specialized(Int) A] { i =>
  def width: Int
  def height: Int
  def apply(x: Int, y: Int):A

  def map(f: (Int, Int) => A) = new Image[A] {
    def width = i.width
    def height = i.height
    def apply(x: Int, y: Int) = f(x,y)
  }

  def ops[A: Pixel] = implicitly[Pixel[A]]
}

