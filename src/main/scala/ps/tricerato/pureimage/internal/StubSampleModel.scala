package ps.tricerato.pureimage.internal

import java.awt.color.ColorSpace
import java.awt.Transparency
import java.awt.image.{Raster, ComponentColorModel, DataBuffer, SampleModel}


private[pureimage] class StubSampleModel(w: Int, h: Int, colors: Int, val colorModel: ComponentColorModel) extends SampleModel(DataBuffer.TYPE_INT, w, h, colorModel.getNumComponents()) {

  private def ??? = throw new Exception("Not implemented")


  def getSampleSize = (1 to colorModel.getNumComponents).map(_ => 8).toArray
  def getSampleSize(i:Int) = 8

  def createDataBuffer() = ???
  def createSubsetSampleModel(a: Array[Int]) = ??? //this//originalSampleModel.createSubsetSampleModel(a)
  def createCompatibleSampleModel(x: Int, y: Int) = colorModel.createCompatibleSampleModel(x, y)//this //??? //createCompatibleSampleModel()//??? //this //originalSampleModel.createCompatibleSampleModel(x, y)
  def setSample(x$1: Int, x$2: Int, x$3: Int, x$4: Int, x$5: java.awt.image.DataBuffer) { ??? }
  def setDataElements(x$1: Int, x$2: Int, x$3: Any, x$4: java.awt.image.DataBuffer) { ??? }
  def getNumDataElements = colorModel.getNumComponents//originalSampleModel.getNumDataElements
  def getDataElements(x: Int, y: Int, out: Any, data: java.awt.image.DataBuffer) = ???
  def getSample(x: Int, y: Int, b: Int, dataBuffer: java.awt.image.DataBuffer) = ???

}
