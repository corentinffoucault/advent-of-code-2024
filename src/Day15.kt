import java.io.BufferedReader
import java.io.File

class Sokoban(
    private var initialRobot: Point2D,
    private var map: List<CharArray>,
    private val movements: List<Point2D>,
) {

    fun doMovements() {
        var place = initialRobot
        movements.forEach { direction ->

            val next = place + direction
            when (map[next.y][next.x]) {
                in "[O]" -> {
                    push(next, direction)?.let { moves ->
                        moves.forEach { (from, to) ->
                            map[to.y][to.x] = map[from.y][from.x]
                            map[from.y][from.x] = '.'
                        }
                        place = next
                    }
                }

                !in "#" -> {
                    place = next
                }
            }
        }
    }

    fun extends() {
        map = map.map { line ->  line.joinToString("") {
            when (it) {
                '#' -> "##"
                'O' -> "[]"
                '.' -> ".."
                '@' -> "@."
                else -> throw IllegalArgumentException("Invalid $it")
            }
        }.toCharArray() }
        initialRobot = Point2D(initialRobot.x*2, initialRobot.y)
    }

    private fun push(
        position: Point2D,
        direction: Point2D
    ): List<Pair<Point2D, Point2D>>? {
        val safePushes = mutableListOf<Pair<Point2D, Point2D>>()
        val seen = mutableSetOf<Point2D>()
        val queue = mutableListOf(position)

        while (queue.isNotEmpty()) {
            val thisPosition = queue.removeFirst()
            if (thisPosition !in seen) {
                seen += thisPosition
                if (direction in setOf(Point2D(0,1), Point2D(0,-1))) {
                    when (map[thisPosition.y][thisPosition.x]) {
                        ']' -> queue.add(thisPosition + Point2D(-1,0))
                        '[' -> queue.add(thisPosition + Point2D(1,0))
                    }
                }
                val nextPosition = thisPosition + direction
                when (map[nextPosition.y][nextPosition.x]) {
                    '#' -> return null
                    in "[O]" -> queue.add(nextPosition)
                }
                safePushes.add(thisPosition to nextPosition)
            }
        }
        return safePushes.reversed()
    }

    fun getGpsSum(): Long {
        var res = 0L
        map.forEachIndexed { y, line -> run {
            line.forEachIndexed {  x, char -> run {
                if (char == 'O' || char =='[') {
                    res += gps(x, y)
                }
            } }
        } }
        return res
    }


    private fun gps(x: Int, y: Int): Long {
        return (100L * y) + x
    }
}

class Day15 {

    fun run(fileName: String) {
        /*val sokoban = createData(fileName)
        sokoban.doMovements()
        println("final result ${sokoban.getGpsSum()}")*/
        val sokoban2 = createData(fileName)
        sokoban2.extends()
        sokoban2.doMovements()
        println("final result ${sokoban2.getGpsSum()}")

    }

    private fun createData(fileName: String): Sokoban {
        val bufferedReader: BufferedReader = File(fileName).bufferedReader()
        val iterator = bufferedReader.lineSequence().iterator()
        val map = mutableListOf<CharArray>()
        var robot = Point2D(0, 0)
        var y = 0
        while (iterator.hasNext()) {
            val line = iterator.next()
            if (line.isEmpty()) {
                break
            }
            val robotx = line.indexOf('@')
            if (robotx != -1) {
                robot = Point2D(robotx, y)
            }
            map.add(line.toCharArray())
            y++
        }

        val movements = mutableListOf<Point2D>()
        while (iterator.hasNext()) {
            val line = iterator.next()
            line.forEach {
                movements.add(getDir(it))
            }
        }
        bufferedReader.close()
        return Sokoban(robot, map, movements)
    }

    private fun getDir(dirChar: Char): Point2D {
        return when (dirChar) {
            '^' -> Point2D(0, -1)
            '<' -> Point2D(-1, 0)
            '>' -> Point2D(1, 0)
            else -> Point2D(0, 1)
        }
    }
}

fun main(args: Array<String>) {
    println("Running the main function");
    Day15().run("resource/day15.txt")
}

// too low 1176366