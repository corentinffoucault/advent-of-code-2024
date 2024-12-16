import java.io.BufferedReader
import java.io.File
import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.floor

private data class Robot2(
    val position: Point2D,
    val velocity: Point2D
) {
    fun move(moves: Int, area: Point2D): Robot2 =
        copy(
            position = (position + (velocity * moves)).wrap(area)
        )

    fun Point2D.wrap(other: Point2D): Point2D {
        val nextX = x % other.x
        val nextY = y % other.y
        return Point2D(
            if (nextX < 0) nextX + other.x else nextX,
            if (nextY < 0) nextY + other.y else nextY
        )
    }

    fun quadrant(midpoint: Point2D): Int =
        when {
            position.x < midpoint.x && position.y < midpoint.y -> 1
            position.x > midpoint.x && position.y < midpoint.y -> 2
            position.x < midpoint.x && position.y > midpoint.y -> 3
            position.x > midpoint.x && position.y > midpoint.y -> 4
            else -> 0
        }

    companion object {
        fun of(input: String): Robot2 =
            Robot2(
                position = Point2D(
                    input.substringAfter("=").substringBefore(",").toInt(),
                    input.substringAfter(",").substringBefore(" ").toInt()
                ),
                velocity = Point2D(
                    input.substringAfterLast("=").substringBefore(",").toInt(),
                    input.substringAfterLast(",").toInt()
                )
            )
    }
}

class Robot(var initialPoint: Point2D, private val speed: Point2D) {
    fun solve(size: Point2D): Point2D {
        for (i in 0..99) {
            initialPoint = Point2D(Math.floorMod(initialPoint.x+speed.x,size.x), Math.floorMod(initialPoint.y+speed.y,size.y))
        }
        return initialPoint
    }

    fun getQuadrant(pos: Point2D, size: Point2D): Int {
        if (pos.x < floor(size.x.toDouble()/2)) {
            if (pos.y < floor(size.y.toDouble()/2)) {
                return 1
            } else if (pos.y >= ceil(size.y.toDouble()/2)) {
                return 2
            }
        } else if (pos.x >= ceil(size.x.toDouble()/2)) {
            if (pos.y < floor(size.y.toDouble()/2)) {
                return 3
            } else if (pos.y >= ceil(size.y.toDouble()/2)) {
                return 4
            }
        }
        return 0
    }
}

class Day14 {

    fun run(fileName: String) {
        val size = Point2D(101, 103)
        val robots = createData(fileName, size)
        var quadrants = mutableListOf(0,0,0,0,0)
        File("day14Res.txt").printWriter().use { out ->
            for (i in 0..10_000) {
                val map = createMap(size);
                robots.forEach {
                    map[it.initialPoint.y].setCharAt(it.initialPoint.x, '1')
                }
                out.println("####################$i")
                map.forEach {
                    if (it.contains("1111111111111111111111111111111")) {
                        println("´´´´´´´´´´´´´´´´´´' $i")
                    }
                    out.println(it)
                }
                out.println("############################################################ $i")
                quadrants = mutableListOf(0,0,0,0,0)
                robots.forEach {
                    val currentPoint = it.solve(size)
                    quadrants[it.getQuadrant(currentPoint, size)]++
                }
            }

        }

        println("final result ${quadrants.subList(1, quadrants.size).fold(1) { acc, i -> acc*i}}")


    }

    fun solvePart2a(fileName: String, area: Point2D = Point2D(101, 103)) {
        var printTheseRobots = createData2(fileName, area)
        File("10_000_robots.txt").printWriter().use { out ->
            repeat(10_000) { move ->
                printTheseRobots = printTheseRobots.map { it.move(1, area) }
                val uniquePlaces = printTheseRobots.map { it.position }.toSet()
                out.println("::::$move::::")
                repeat(area.y) { y ->
                    repeat(area.x) { x ->
                        out.print(if (Point2D(x, y) in uniquePlaces) "#" else '.')
                    }
                    out.println()
                }
            }
        }
    }

    fun solvePart2b(fileName: String, area: Point2D = Point2D(101, 103)): Int {
        var moves = 0
        var robotsThisTurn = createData2(fileName, area)
        do {
            moves++
            robotsThisTurn = robotsThisTurn.map { it.move(1, area) }
        } while (robotsThisTurn.distinctBy { it.position }.size != robotsThisTurn.size)
        return moves
    }

    private fun createMap(size: Point2D): MutableList<StringBuilder> {
        var map = mutableListOf<StringBuilder>()
        for (i in 0..size.y) {
            map.add(StringBuilder(".".repeat(size.x)))
        }
        return map
    }

    private fun createData(fileName: String, size: Point2D): List<Robot> {
        val bufferedReader: BufferedReader = File(fileName).bufferedReader()
        val robots = mutableListOf<Robot>()
        bufferedReader.use {
            it.forEachLine { line: String ->
                run {
                    val reg = Regex("p=([0-9]+),([0-9]+) v=(-?[0-9]+),(-?[0-9]+)")
                    val res = reg.find(line)
                    val x0 = res?.groups?.get(1)?.value?.toInt()
                    val y0 = res?.groups?.get(2)?.value?.toInt()
                    val cx = res?.groups?.get(3)?.value?.toInt()
                    val cy = res?.groups?.get(4)?.value?.toInt()
                    if (x0 != null && y0 != null && cx != null && cy != null) {
                        robots.add(Robot(Point2D(Math.floorMod(x0,size.x), Math.floorMod(y0,size.y)), Point2D(cx, cy)))
                    }
                }
            }
        }
        return robots
    }

    private fun createData2(fileName: String, size: Point2D): List<Robot2> {
        val bufferedReader: BufferedReader = File(fileName).bufferedReader()
        val robots = mutableListOf<Robot2>()
        bufferedReader.use {
            it.forEachLine { line: String ->
                run {
                    val reg = Regex("p=([0-9]+),([0-9]+) v=(-?[0-9]+),(-?[0-9]+)")
                    val res = reg.find(line)
                    val x0 = res?.groups?.get(1)?.value?.toInt()
                    val y0 = res?.groups?.get(2)?.value?.toInt()
                    val cx = res?.groups?.get(3)?.value?.toInt()
                    val cy = res?.groups?.get(4)?.value?.toInt()
                    if (x0 != null && y0 != null && cx != null && cy != null) {
                        robots.add(Robot2(Point2D(Math.floorMod(x0,size.x), Math.floorMod(y0,size.y)), Point2D(cx, cy)))
                    }
                }
            }
        }
        return robots
    }
}

fun main(args: Array<String>) {
    println("Running the main function");
    Day14().run("resource/day14.txt")
    Day14().solvePart2a("resource/day14.txt")
    Day14().solvePart2b("resource/day14.txt")
}