package connect_n

final class Solver(val config:GameConfig) {
    private val enemy = Player opposing config.player
    private val board = new Board(config.rows, config.cols)
    private var state:GameState = null
    
    private var solverThread:Thread = null

    def move():Int = {
        val m:Int = config.cols / 2
        board(m) = Token(config.player)
        state = new GameState(m, config.player)
        return m
    }
    def move(i:Int):Int = {
        // Update state from last move
        if (state == null)
            state = new GameState(i, enemy)
        else
            state = state.children.getOrElse(i, new GameState(i,enemy))

        // Set up our timer
        this.solverThread = Thread.currentThread
        this.setTimer()
        
        // Search for a good move
        var bestMove:Int = config.cols / 2
        try {
            for (i <- 1 to (config.rows * config.cols)) {
                bestMove = search(state,i)
            }
        } catch {
            case ex:InterruptedException => // Ignore it; just a control flow aid
        }
        
        // Update state with our move
        state = state.children.getOrElse(bestMove, new GameState(bestMove, config.player))

        return bestMove
    }

    private def search(gameState:GameState, depth:Int):Int =
    if (Thread.interrupted) {
        throw new InterruptedException
    } else {
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

    private def stopSearching() = solverThread.interrupt()

    private def setTimer() = new java.util.Timer().schedule(
        new java.util.TimerTask { def run() = Solver.this.stopSearching() },
        this.config.timeLimit*1000 - 100
    )
}
