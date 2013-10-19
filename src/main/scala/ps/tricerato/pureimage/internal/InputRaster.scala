package ps.tricerato.pureimage.internal

import java.awt.image._
import java.util.Arrays

private[pureimage] class InputRaster(colorModel: ColorModel, sampleModel: SampleModel, w: Int, h: Int, data: Array[Int]) extends WritableRaster(sampleModel, emptyDataBuffer, new java.awt.Point(0,0)) {
    this.width = w
    this.height = h
    this.minX = 0
    this.minY = 0

    override def setPixel(x: Int, y: Int, v: Array[Int]) {
      set(x,y,v)
    }

    @inline private def set(x: Int, y: Int, v: Array[Int]) {
      val i = (y * w) + x

      val o = if (v.length == 1) {
        0 | v(0)
      } else if (v.length == 3) {
        0 | v(0) | ( v(1) << 8 ) | ( v(2) << 16 )
      } else {
        0 | v(0) | ( v(1) << 8 ) | ( v(2) << 16 ) | (v(3) << 24)
      }

      data(i) = o

    }

    override def setRect(x: Int, y: Int, src: Raster) {
      val v = new Array[Int](4)
      val w = src.getWidth
      val h = src.getHeight

      var i = 0
      while (i < w) {
        var j = 0
        while (j < h) {
          src.getPixel(i, j, v)
          set(x + i, y + j, v)
          j += 1
        }
        i += 1
      }
    }

    override def setDataElements(x: Int, y: Int, w: Int, h: Int, dataObj: Object) {
      val data = dataObj.asInstanceOf[Array[Byte]]
      val bands = sampleModel.getNumBands

      var buffer = new Array[Byte](bands)
      var i = 0
      while (i < w) {
        var j = 0
        while (j < h) {
          val idx = bands * ((j * h) + i)
          val destX = x + i
          val destY = y + j

          buffer = Arrays.copyOfRange(data, idx, idx + bands)

          val rs = colorModel.getRed(buffer).toByte
          val gs = colorModel.getGreen(buffer).toByte
          val bs = colorModel.getBlue(buffer).toByte
          val as = colorModel.getAlpha(buffer).toByte

          val dest = (destY * this.w) + destX
          // r(dest) = rs
          // g(dest) = gs
          // b(dest) = bs
          // a(dest) = as

          j += 1
        }
        i += 1
      }
    }
  }