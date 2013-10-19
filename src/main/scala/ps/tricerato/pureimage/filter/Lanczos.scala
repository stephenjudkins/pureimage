package ps.tricerato.pureimage.filter

import ps.tricerato.pureimage._
object Lanczos {
  val a = 2

  import math._

  private def L(x: Float):Float= if (x == 0F) {
    1F
  } else if (abs(x) > a) {
    0F
  } else {
    sinc(x) * sinc(x / a)
  }


  private def sinc(x: Float) = (sin(Pi * x) / (Pi * x)).toFloat

  val RANGE = (-2 * a to 2 * a) map { _.toFloat / 2 }

  val COORDS = for {
    i <- RANGE
    j <- RANGE
  } yield (i, j)

  val LANCZOS_UNNORMALIZED = COORDS map { case (i,j) => (i, j) -> L(0 - i) * L(0 - j) }

  val SUM = LANCZOS_UNNORMALIZED.map(_._2).sum

  val LANCZOS = LANCZOS_UNNORMALIZED map { case (k,v) => k -> v / SUM } filter { case (k, v) => abs(v) > 0.001 }

  def apply[I : Pixel](scale: Float)(image: Image[I]) = {
    val lanczosByCoord = LANCZOS groupBy { case ((i,j), v) => ((i * scale).toInt, (j * scale).toInt) }

    val lanczos = lanczosByCoord.toSeq map { case ((i,j), vals) =>
      (i, j) -> vals.map(_._2).sum
    } filter { case (_, v) => abs(v) > 0.001 }

    val (ij, ls) = lanczos.unzip
    val (is, js) = ij.unzip

    val (ia, ja, la) = (is.toArray, js.toArray, ls.toArray)

    val mx = image.width - 1
    val my = image.height - 1

    val ops = image.ops
    image map { (x, y) =>
      var sum = ops.zero
      var index = 0
      val length = ia.length

      while (index < length) {
        val l = la(index)
        val i = ia(index)
        val j = ja(index)
        val x0 = between(0, x + i, mx)
        val y0 = between(0, y + j, my)

        val p = image(x0, y0)

        sum = ops.sum(sum, ops.fade(p, l))

        index += 1
      }

      // println(sum)
      sum
    }
  }

}
