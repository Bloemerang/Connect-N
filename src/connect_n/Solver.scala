package connect_n

final class Solver(val config:GameConfig) {
    private val enemy = Player opposing config.player
    private val board = new Board(config.rows, config.cols)
    private var state:GameState = null

    def move():Int = {
        val m:Int = config.cols / 2
        board(m) = Token(config.player)
        state = new GameState(m, config.player)
        return m
    }
    def move(i:Int):Int = {
        if (state == null)
            state = new GameState(i, enemy)
        else
            state = state.children.getOrElse(i, new GameState(i,enemy))

        

        return 0 // TODO
    }

    private def eval(gameState:GameState):Int = {
        val move = new Point(board.topRowOf(gameState.move), gameState.move)
        val plToken = Token(gameState.player)

        if (move.y-config.n >= -1 &&
            (((move.y+1) until (move.y-config.n) by -1) forall { i =>
                board(move.x,i) == plToken
            }))
            return 1
        else if ( (move.x+config.n) >= config.cols &&
                 ((move.x+1 until (move.x+config.n)) forall { i =>
                board(i, move.y) == plToken
            }))
            return 1
        else if ( (move.x-config.n) >= -1 &&
                 ((move.x-1 until (move.x-config.n)) forall { i =>
                board(i, move.y) == plToken
            }))
            return 1
        else
            return 0
    }

    private def search(gameState:GameState, depth:Int):Int = {
        val utility = eval(gameState)
        if (utility == 1)
            utility
        else if ( depth == 0 || board.isFull )
            0
        else {
            var (utility, comp) = gameState.player match {
                case config.player => (Int.MinValue, math.max _)
                case _             => (Int.MaxValue, math.min _)
            }

            for ( state <- gameState.actions(board) )
                utility = comp( utility, search(state, depth-1) )
            
            utility
        }
    }
}
