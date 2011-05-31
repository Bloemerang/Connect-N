package connect_n

import java.io.{BufferedReader, InputStreamReader}

object Main {
    val Name = "kStone"

    val in = new BufferedReader(new InputStreamReader(System.in))

    def main(args:Array[String]) = {

        println(Name) // Announce ourself to the referee

        val config = GameConfig(in)     // Read the game configuration
        val solver = new Solver(config) // Make the solver



        config.player match {
            case Player.Two => gameLoop(input)
            case Player.One => {
                println(solver.move())
                gameLoop(input)
            }
        }
    }

    def gameLoop(input:Int) = input match {
        case i if i >= 0 && i < solver.config.cols => {
            println(solver.move(i))
            gameLoop(input)
        }
        case i if i < 0 && i > -4  => onGameEnd(in.readLine())
        case _                     => sys.error("Illegal input from referee")
    }

    def onGameEnd(status:String) = ;

    def input = {
        val splitInput = in.readLine().split(" ");
        assert(splitInput.length == 1)
        Integer.parseInt(splitInput(0))
    }
}