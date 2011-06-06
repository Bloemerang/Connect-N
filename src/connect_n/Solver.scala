package connect_n

final class Solver(val config:GameConfig) {
    private val enemy = Player opposing config.player
    private val board = new Board(config.rows, config.cols)
    private var currentState:GameState = null
    
    private var solverThread:Thread = null
    private var moveCount = 0

    def move():Int = {
        val m:Int = config.cols / 2
        board(m) = Token(config.player)
        currentState = new GameState(m, config.player)
        return m
    }
    def move(i:Int):Int = {
        // Update state from last move
        if (currentState == null)
            currentState = new GameState(i, enemy)
        else
            currentState = currentState.children.getOrElse(i, new GameState(i,enemy))
        board(i) = Token(enemy)

        // Set up our timer
        this.solverThread = Thread.currentThread
        this.setTimer()
        
        // Search for a good move
        var bestMove:Int = config.cols / 2
        try {
            for (i <- 2 to (config.rows * config.cols)) {
                var utility = Int.MinValue
                for (state <- currentState.actions(board)) {
                    val minimax = search(state, i)
                    if (minimax > utility) {
                        utility = minimax
                        bestMove = state.move
                    }
                }
            }
        } catch {
            case ex:InterruptedException => // Ignore it; just a control flow aid
        }
        
        // Update state with our move
        currentState = currentState.children.getOrElse(bestMove, new GameState(bestMove, config.player))
        board(bestMove) = Token(config.player)

        System.err.println(board)

        return bestMove
    }

    private def search(gameState:GameState, depth:Int):Int =
    if (Thread.interrupted) {
        throw new InterruptedException
    } else {
        val (utility,isTerminal) = eval(gameState)
        if (isTerminal || depth==0 || board.isFull)
            utility
        else {
            var (utility, comp) = gameState.player match {
                case config.player => (Int.MaxValue, math.min _)
                case _             => (Int.MinValue, math.max _)
            }

            for ( state <- gameState.actions(board) )
                utility = comp( utility, search(state, depth-1) )
            
            utility
        }
    }

    private def eval(gameState:GameState):(Int,Boolean) = {
        val move = new Point(gameState.move, board.topRowOf(gameState.move))
        val plToken = Token(gameState.player)
        val utility = gameState.player match {
            case config.player => 1
            case _             => -1
        }

        if (move.row-config.n >= -1 &&
            (((move.row-1) until (move.row-config.n) by -1) forall { i =>
                board(move.col,i) == plToken
            }))
            return (utility,true)
        else if ( (move.col+config.n) <= config.cols &&
                 ((move.col+1 until (move.col+config.n)) forall { i =>
                board(i, move.row) == plToken
            }))
            return (utility,true)
        else if ( (move.col-config.n) >= -1 &&
                 ((move.col-1 until (move.col-config.n) by -1) forall { i =>
                board(i, move.row) == plToken
            }))
            return (utility,true)
        else
            return (0,false)
    }

    private def stopSearching() = solverThread.interrupt()

    private def setTimer():Unit = {
        //moveCount += 1
        //if (moveCount > 2) return
        new java.util.Timer().schedule(
        new java.util.TimerTask { def run() = Solver.this.stopSearching() },
        this.config.timeLimit*1000 - 200
    )}
}

final class Point(val col:Int, val row:Int)