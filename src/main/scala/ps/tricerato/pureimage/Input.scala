package ps.tricerato.pureimage

import javax.imageio._
import javax.imageio.stream._
import java.io._
import java.awt.image._
import collection.JavaConverters._
import internal._

object Input {
  def apply(data: Array[Byte]):Either[ReadError, LoadedImage] = {
    val imageInput = new MemoryCacheImageInputStream(new ByteArrayInputStream(data))
    val reader = ImageIO.getImageReaders(imageInput).asScala.collect({ case i:ImageReader => i }).toSeq.headOption

    reader map { reader => withReader(reader, imageInput) } toRight UnsupportedImageType
  }

  private def withReader(reader: ImageReader, input: ImageInputStream):LoadedImage = {
    reader.setInput(input)
    val imageType = reader.getImageTypes(0).asScala.toSeq.head
    val colorModel = imageType.getColorModel
    val width = reader.getWidth(0)
    val height = reader.getHeight(0)

    val backing = new Array[Int](width * height)

    val raster = new InputRaster(colorModel, imageType.getSampleModel, width, height, backing)

    val underlying = new BufferedImage(
      colorModel,
      raster,
      false,
      new java.util.Hashtable()
    )

    val param = (new ImageReadParam).tap { _.setDestination(underlying) }

    try {
      reader.read(0, param)
    } finally {
      reader.dispose()
    }


    val components = imageType.getNumComponents

    val (sW, sH) = (width, height)

    if (components == 1) {
      GrayImage(
        new Image[Gray] {
          val width = sW; val height = sH
          def apply(x: Int, y: Int) = Gray(backing((y * width) + x))
        }
      )
    } else if (components == 3) {
      RGBImage(
        new Image[RGB] {
          val width = sW; val height = sH
          def apply(x: Int, y: Int) = RGB(backing((y * width) + x))
        }
      )
    } else {
      RGBAImage(
        new Image[RGBA] {
          val width = sW; val height = sH
          def apply(x: Int, y: Int) = RGBA(backing((y * width) + x))
        }
      )
    }


  }

}

sealed trait LoadedImage
case class RGBImage(image: Image[RGB]) extends LoadedImage
case class RGBAImage(image: Image[RGBA]) extends LoadedImage
case class GrayImage(image: Image[Gray]) extends LoadedImage
