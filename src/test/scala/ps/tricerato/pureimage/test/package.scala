package ps.tricerato.pureimage

import org.apache.commons.io.IOUtils


package object test {
  System.setProperty("java.awt.headless", "true")

  def resource(name: String) = IOUtils.toByteArray(getClass.getResourceAsStream(name))

  def zardozJpeg = resource("/zardoz.jpeg")
}