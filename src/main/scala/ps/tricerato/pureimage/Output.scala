package ps.tricerato.pureimage

import javax.imageio.{ImageWriteParam, IIOImage, ImageWriter, ImageIO}
import collection.JavaConverters._
import java.awt.color.ColorSpace
import java.awt.image._
import java.awt.Transparency
import java.io._

import internal._

trait OutputFormat
object JPEG extends OutputFormat
object PNG extends OutputFormat
object GIF extends OutputFormat

trait Output[I, O <: OutputFormat] {
  def apply(i: Image[I], o: O):Array[Byte]
}

object Output {
  def apply[I, O <: OutputFormat](image: Image[I], format: O)(implicit output: Output[I,O]) = output(image, format)

  def getColorModel(id: Int) = {
    val colorSpace = ColorSpace.getInstance(id)
    new ComponentColorModel(colorSpace, false, false, Transparency.OPAQUE, DataBuffer.TYPE_INT) {
      override def isCompatibleRaster(r: Raster) = true
    }
  }

  implicit object outputPNG_RGB extends Output[RGB, PNG.type] {
    def apply(i: Image[RGB], o: PNG.type) = {
      outputPNG(i.width, i.height, ColorSpace.CS_sRGB, (x:Int,y:Int) => i(x,y).i)
    }
  }

  implicit object outputPNG_Gray extends Output[Gray, PNG.type] {
    def apply(i: Image[Gray], o: PNG.type) = {
      outputPNG(i.width, i.height, ColorSpace.CS_GRAY, (x:Int,y:Int) => i(x,y).i)
    }
  }

  private def outputPNG(width: Int, height:Int, colorSpace: Int, func: (Int, Int)=>Int): Array[Byte] = {
    val writer = ImageIO.getImageWritersByFormatName("png").asScala.toSeq.head

    val colorModel = getColorModel(colorSpace)

    val colors = colorModel.getNumComponents

    val sampleModel = new StubSampleModel(width, height, colors, colorModel)

    val raster = new OutputRaster(sampleModel, width, height, 0, 0, func, colors)

    val buffered = new OutputBufferedImage(
      sampleModel.colorModel,
      raster
    )

    (new ByteArrayOutputStream).tap({ o =>
      // type-safety FTW. :-/
      writer.setOutput(ImageIO.createImageOutputStream(o))
      val iioImage = new IIOImage(buffered, null, null)
      val param = writer.getDefaultWriteParam
      writer.write(null, iioImage, param)
    }).toByteArray
  }
}