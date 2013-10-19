package ps.tricerato.pureimage.internal
import java.awt.color.ColorSpace
import java.awt.image._


private[pureimage] case class OutputRaster(
   ourSampleModel: SampleModel,
   w: Int,
   h: Int,
   x: Int,
   y: Int,
   rgba: (Int, Int) => Int,
   perPixel: Int,
   offsetX: Int = 0,
   offsetY: Int = 0
) extends WritableRaster(ourSampleModel, emptyDataBuffer, new java.awt.Point(0,0)) {
  this.numBands = perPixel

  override def getPixels(x0: Int, y0: Int, w0: Int, h0: Int, outArray: Array[Int]) = {
    val out = if (outArray == null) {
      new Array[Int](w0 * h0 * perPixel)
    } else {
      outArray
    }

    var i = 0
    while (i < w0) {
      var j = 0
      while (j < h0) {
        val x1 = i + x + offsetX
        val y1 = j + y + offsetY
        val idx = (j * w0 + i) * perPixel

        val p = rgba(x1, y1)

        out(idx) = p & 0xff
        out(idx + 1) = (p >> 8) & 0xff
        out(idx + 2) = (p >> 16) & 0xff

        j += 1
      }
      i += 1
    }


    out
  }

  override def createChild(parentX: Int, parentY: Int, w: Int, h: Int, minX: Int, minY: Int, bandList: Array[Int]) =
    copy(offsetX = offsetX + parentX, offsetY = offsetY + parentY, w = w, h = h)
}

private[pureimage] class OutputBufferedImage(cm: ColorModel, raster: OutputRaster) extends BufferedImage(cm, raster, false, new java.util.Hashtable()) {
  override def getData(rect: java.awt.Rectangle) = {
    raster.copy(x = rect.getX.toInt, y = rect.getY.toInt, w = rect.getWidth.toInt, h = rect.getHeight.toInt)
  }

}

