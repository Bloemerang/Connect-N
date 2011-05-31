package connect_n

object Player extends Enumeration {
    type Number = Value
    val One  = Value(0)
    val Two  = Value(1)
}

object GameConfig {
    def apply(in:java.io.BufferedReader) = {
        val confValues = in.readLine().split(" ");
        assert(confValues.length == 5)

        new GameConfig(
            Integer.parseInt(confValues(0)),
            Integer.parseInt(confValues(1)),
            Integer.parseInt(confValues(2)),
            Player(Integer.parseInt(confValues(3))),
            Integer.parseInt(confValues(4))
        )
    }
}

final class GameConfig(
    rows:Int,             // # rows in the game
    cols:Int,             // # columns in the game
    n:Int,                // # of consecutive tokens needed to win
    player:Player.Number, // The player number. Player.First goes first.
    timeLimit:Int        // The time limit, in seconds, for making a move
)
