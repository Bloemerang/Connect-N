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
                System.out.flush()
                gameLoop(input)                // and then loop
            }
        }
    } // main()

    def gameLoop(input:Int):Unit = input match {
        case i if i >= 0 && i < solver.config.cols => {
            println(solver.move(i))
            System.out.flush()
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
}