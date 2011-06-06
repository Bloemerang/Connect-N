package connect_n

object Player extends Enumeration {
    type Number = Value
    val Max  = Value(1)
    val Min  = Value(2)

    def opposing(other:Number) = other match {
        case Max => Min
        case Min => Max
    }
}

object GameConfig {
    def apply(in:java.io.BufferedReader) = {
        val confValues = in.readLine().split(" ");
        assert(confValues.length == 5)

        new GameConfig(
            Integer.parseInt(confValues(0)),
            Integer.parseInt(confValues(1)),
            Integer.parseInt(confValues(2)),
            Player(Integer.parseInt(confValues(3))+1),
            Integer.parseInt(confValues(4))
        )
    }
}

final class GameConfig(
    val rows:Int,             // # rows in the game
    val cols:Int,             // # columns in the game
    val n:Int,                // # of consecutive tokens needed to win
    val player:Player.Number, // The player number. Player.First goes first.
    val timeLimit:Int        // The time limit, in seconds, for making a move
)
