package connect_n

import collection.immutable.Traversable
import collection.mutable.Map

final class GameState(val move:Int, val player:Player.Number) {
    var utility:Int = Integer.MAX_VALUE;
    val children = Map.empty[Int,GameState]

    def actions(board:Board):TraversableOnce[GameState] = new Actions(board)

    private class Actions(board:Board) extends Traversable[GameState] {
        def foreach[U](f: GameState => U) = {
            val otherPlayer = Player opposing player

            for ( i <- 0 until board.cols; if !board.isFull(i) ) {
                board(i) = Token(player)
                try {
                    f(children.getOrElseUpdate(i, new GameState(i, otherPlayer)))
                } finally {
                    board(i) = Token.None
                }
            }
        } // foreach()
    } // class Actions
} // class GameState
