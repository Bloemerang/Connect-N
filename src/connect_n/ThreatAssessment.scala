package connect_n

object ThreatAssessment {
    object PlayerThreats {
        def assess[T](move:Threat[T]#CellType) = {

        }
    }

    final class PlayerThreats(
        val vert:Map[Int,List[StraightThreat]],
        val horiz:Map[Int,List[StraightThreat]],
        val diagUp:Map[Int,List[DiagonalThreat]],
        val diagDn:Map[Int,List[DiagonalThreat]]
    ) {
        def this() = this(Map(),Map(),Map(),Map())
    }
}
import ThreatAssessment.PlayerThreats

class ThreatAssessment private (
    p1Threats:PlayerThreats,
    p2Threats:PlayerThreats
) {
    def this() = this(new PlayerThreats, new PlayerThreats)

    def assessmentForMove(move:Int, player:Player.Number, board:Board) = {
        // Create the assessment for the playing player
        val playerThreats = threatsForPlayer(player)
    }


    private def threatsForPlayer(player:Player.Number) = player match {
        case Player.Max => p1Threats
        case Player.Min => p2Threats
    }
}
