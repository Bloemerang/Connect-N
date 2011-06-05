package connect_n

final class Solver(
    val config:GameConfig,
    utilityFxn: (Board,GameState) => Int
) {
    private val board = new Board(config.rows, config.cols)
    private val utilityOf: GameState => Int   = utilityFxn(board,_)

    def move():Int = 0
    def move(i:Int):Int = 0

    private def search(gameState:GameState, depth:Int):Int =
        if ( depth == 0 || board.isFull ) utilityOf(gameState)
        else {
            var (utility, comp) = gameState.player match {
                case config.player => (Int.MinValue, math.max _)
                case _             => (Int.MaxValue, math.min _)
            }

            for ( state <- gameState.actions(board) )
                utility = comp( utility, search(state, depth-1) )
            
            return utility
    }
}
