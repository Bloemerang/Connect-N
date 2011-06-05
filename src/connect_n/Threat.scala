package connect_n

object Threat {
    object Cell { def apply(row:Byte, col:Byte) = new Cell(row,col) }
    
    class Cell(val row:Byte, val col:Byte)
}

class Threat private (cells:Array[Threat.Cell]) {
    def this(cell_1:Threat.Cell, cell_2:Threat.Cell, length:Int) = {
        this(new Array[Threat.Cell](length))
        // TODO: Stopping point
    }
}
