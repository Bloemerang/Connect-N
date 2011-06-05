package connect_n

import collection.immutable.Traversable

final class GameState(val move:Int, player:Player.Number) {
    var utility:Int = Integer.MAX_VALUE;

    def actions(board:Board):TraversableOnce[GameState] = new Actions(board)

    private class Actions(board:Board) extends Traversable[GameState] {
        def foreach[U](f: GameState => U) = {
            val otherPlayer = Player opposing player

            for ( i <- 0 until board.cols; if !board.isFull(i) ) {
                board(i) = Token(player)
                val nextState = new GameState(i, otherPlayer)
                try {
                    f(nextState)
                } finally {
                    board(i) = Token.None
                }
            }
        } // foreach()
    } // class Actions
} // class GameState
