package connect_n

final object Token extends Enumeration {
    val None = Value(0)
    val Max  = Value(1)
    val Min  = Value(2)

    def apply(player:Player.Number):Value = Token(player.id)
}

final class Board(val rows:Int, val cols:Int) {
    require(cols < (java.lang.Long.SIZE / 2))
    val board = new Array[Long](rows)
    val heights = new Array[Int](cols)

    // Just here to verify that board and heights are initialized to zero
    require( (true /: board)   { (l:Boolean, r:Long) => l && (r == 0) })
    require( (true /: heights) { (l:Boolean, r:Int) => l && (r == 0) })

    def update(col:Int, token:Token.Value) = {
        assert(col < this.cols)
        token match {
            case Token.None => {
                assert(heights(col) > 0)

                heights(col) -= 1
                board(heights(col)) &= ~tokenToCol(0x3, col)
            }
            case _ => {
                assert( !isFull(col) )
                assert( (board(heights(col)) & tokenToCol(0x3, col)) == 0,
                        "State corruption: attempt to place a token at an occupied space" )
                board(heights(col)) |= tokenToCol(token.id, col)
                heights(col) += 1
            }
        }
    }

    def isFull(col:Int) = {
        assert(col < this.cols)

        heights(col) == this.rows
    }

    private def tokenToCol(token:Long, col:Int):Long = token << (col << 1)

    override def toString = {
        val buf = new StringBuffer()

        def hr() = {
            buf append '+'
            for (i <- 0 until cols) { buf append '-' }
            buf append '+' append '\n'
        }

        hr()
        for (row <- board.reverseIterator) {
            buf append '|'
            for (i <- 0 until cols*2 by 2 ) {
                buf append (Token( ((row & (3 << i)) >>> i).toInt ) match {
                    case Token.None => " "
                    case Token.Max  => "X"
                    case Token.Min  => "O"
                })
            }
            buf append '|' append '\n'
        }
        hr()

        buf toString
    } // toString()
} // class Board
