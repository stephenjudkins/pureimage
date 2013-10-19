package ps.tricerato.pureimage

import java.awt.image._

package object internal {
  private[pureimage] def emptyDataBuffer = new DataBufferInt(1)
}
