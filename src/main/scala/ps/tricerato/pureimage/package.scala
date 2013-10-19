package ps.tricerato

package object pureimage {
  private [pureimage] implicit class AnyOps[A](val a: A) extends AnyVal {
    def |>[B](f: A => B):B = f(a)
    def tap[B](f: A => B) = {
      f(a)
      a
    }

  }

  @inline private [pureimage] def between(floor: Int, x: Int, ceil: Int) = {
    if (floor > x) {
      floor
    } else if (ceil < x) {
      ceil
    } else {
      x
    }
  }




}