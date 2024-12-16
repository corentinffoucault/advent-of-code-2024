import java.io.BufferedReader
import java.io.File
import kotlin.math.abs

class Sokoban(private val robot: Point2D, private val boxAt0: Set<Point2D>, private val walls: Set<Point2D>, private val size: Point2D) {
    fun solve(movement: List<String>): Long {
        println("robot $robot size $size  boxes: ")
        boxAt0.forEach{ println(it) }
        walls.forEach{ println(it) }
        println("$movement")
        return 0L
    }
}

class Day15 {

    fun run(fileName: String) {
        val ( sokoban, movement ) = createData(fileName)
        println("final result ${sokoban.solve(movement) }}")
    }

    private fun createData(fileName: String): Pair<Sokoban, List<String>> {
        val bufferedReader: BufferedReader = File(fileName).bufferedReader()
        val iterator = bufferedReader.lineSequence().iterator()
        var sokoban: Sokoban? = null
        val movements = mutableListOf<String>()
        var robot = Point2D(0, 0)
        val boxes = mutableSetOf<Point2D>()
        val walls = mutableSetOf<Point2D>()
        var y = 0
        var x = 0
        val regBox = Regex("(O)")
        val regWall = Regex("(#)")
        while(iterator.hasNext()) {
            val line = iterator.next()
            if (line.isEmpty()) {
                sokoban = Sokoban(robot, boxes, walls, Point2D(x, y))
                break
            }
            var robotx = line.indexOf('@')
            if (robotx!=-1) {
                robot = Point2D(robotx, y)
            }
            val boxesReg = regBox.findAll(line)
            boxesReg.forEach {
                boxes.add(Point2D(it.range.first, y))
            }
            val wallReg = regWall.findAll(line)
            wallReg.forEach {
                walls.add(Point2D(it.range.first, y))
            }
            x = line.length
            y++
        }
        while(iterator.hasNext()) {
            val line = iterator.next()
            movements.add(line)
        }
        bufferedReader.close()
        if (sokoban != null) {
            return Pair(sokoban, movements)
        }
        throw Error("bad parsing")
    }
}

fun main(args: Array<String>) {
    println("Running the main function");
    Day15().run("resource/day15.txt")
}