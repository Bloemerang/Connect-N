package connect_n

import java.io.{BufferedReader, InputStreamReader}

final object Main {
    val Name = "kStone"

    val in = new BufferedReader(new InputStreamReader(System.in))
    var solver:Solver = null

    def main(args:Array[String]) = {
        println(Name) // Announce ourself to the referee

        val config = GameConfig(in) // Read the game configuration
        solver = new Solver(config) // Make the solver

        // Start playing
        config.player match {
            case Player.Min => gameLoop(input) // Wait for input and then loop
            case Player.Max => {
                println(solver.move())         // Make the first move
                gameLoop(input)                // and then loop
            }
        }
    } // main()

    def gameLoop(input:Int):Unit = input match {
        case i if i >= 0 && i < solver.config.cols => {
            println(solver.move(i))
            gameLoop(this.input()) // recurse
        }
        case i if i < 0 && i > -4  => onGameEnd(in.readLine())
        case _ => System.err.println("Illegal input from referee")
    }

    def onGameEnd(status:String) = null;

    def input() = {
        val splitInput = in.readLine().split(" ");
        assert(splitInput.length == 1)
        Integer.parseInt(splitInput(0))
    }

    def test() = {
        val board = new Board(6,7)
        val game = new GameState(3, Player.Max)

        def iterate(n:Int):Unit = if (n > 0) {
            var i = 0
            for (state <- game.actions(board)) i match {
                case x if x >= n => {
                    println("About to return with n=%d, i=%d".format(n,i))
                    println(board)
                    return
                }
                case _ => {
                    println("About to iterate with n=%d, i=%d".format(n,i))
                    println(board)
                    iterate(n-1)
                    i += 1
                }
            }
        } else {
            println("Hit base case with board:")
            println(board)
        }

        iterate(3)
        println(board)

        exit(0)
    }
}