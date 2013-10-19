package ps.tricerato.pureimage

sealed trait ReadError
case object UnsupportedImageType extends ReadError
case class ReadException(e: Exception) extends ReadError

