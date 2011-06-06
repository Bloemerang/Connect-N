package connect_n

final class Point(val x:Int, val y:Int)

abstract class Threat[ThreatType] protected (val size:Int) {
    type CellType
    
    def add(cell:CellType):Option[ThreatType]
    def contains(cell:CellType):Boolean
    def complete:Boolean
}

final class StraightThreat(
    start:Int,
    stop:Int,
    size:Int
) extends Threat[StraightThreat](size) {
    type CellType = Int

    def add(cell:CellType) = contains(cell) match {
        case true  => Some( new StraightThreat(start, stop, size+1) )
        case false => None
    }

    def contains(cell:CellType) = ( cell >= start && cell < stop )

    def complete = ( size == (math.abs(stop - start)) )
}

final class DiagonalThreat(
    slope:Int,
    yIntrcpt:Int,
    startX:Int,
    stopX:Int,
    size:Int
) extends Threat[DiagonalThreat](size) {
    type CellType = Point

    def add(cell:CellType) = contains(cell) match {
        case true  => Some( new DiagonalThreat(slope, yIntrcpt, startX, stopX, size+1))
        case false => None
    }

    def contains(cell:CellType) =
        (cell.y == slope*cell.x + yIntrcpt) && (cell.x >= startX && cell.x < stopX)

    def complete = ( size == (stopX - startX) )
}