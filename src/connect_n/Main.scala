package connect_n

import java.io.{BufferedReader, InputStreamReader}

final object Main {
    val Name = "kStone"

    val in = new BufferedReader(new InputStreamReader(System.in))
    var solver:Solver = null

    def main(args:Array[String]) = {

        val board = new State(6,7)
        println(board)
        board(3) = Player.One
        println(board)
        board(3) = Player.Two
        println(board)
        board(4) = Player.One
        println(board)
        board(2) = Player.Two
        println(board)

        exit(0)

        println(Name) // Announce ourself to the referee

        val config = GameConfig(in) // Read the game configuration
        solver = new Solver(config) // Make the solver

        // Start playing
        config.player match {
            case Player.Two => gameLoop(input) // Wait for input and then loop
            case Player.One => {
                println(solver.move())         // Make the first move
                gameLoop(input)                // and then loop
            }
        }
    } // main()

    def gameLoop(input:Int):Unit = input match {
        case i if i >= 0 && i < solver.config.cols => {
            println(solver.move(i))
            gameLoop(input) // recurse
        }
        case i if i < 0 && i > -4  => onGameEnd(in.readLine())
        case _ => System.err.println("Illegal input from referee")
    }

    def onGameEnd(status:String) = null;

    def input = {
        val splitInput = in.readLine().split(" ");
        assert(splitInput.length == 1)
        Integer.parseInt(splitInput(0))
    }
}