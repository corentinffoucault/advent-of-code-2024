import java.io.BufferedReader
import java.io.File
import kotlin.math.abs

class Maze20(private val start: Point2D, private val end: Point2D, val map: List<String>) {

    private fun inRange(point: Point2D): Boolean {
        return point.x >= 0 && point.x < map[0].length
                && point.y >= 0 && point.y < map.size
                && map[point.y][point.x] != '#'
    }

    fun solve(): Map<Point2D, Long> {
        val queue = mutableListOf(start)
        val seen = mutableMapOf<Point2D, Long>()
        var index = 0L
        while (queue.isNotEmpty()) {
            val currentPoint = queue.removeFirst()
            if (!seen.contains(currentPoint)) {
                seen[currentPoint] = index
                if (currentPoint == end) {
                    return seen
                }

                getNext(currentPoint)
                    .filter { inRange(it) }
                    .forEach { queue.add(it) }
                index++
            }
        }
        return seen
    }

    private fun getNext(point: Point2D): List<Point2D> {
        return listOf(point + Point2D(0, 1), point + Point2D(0, -1), point + Point2D(-1, 0), point + Point2D(1, 0))
    }
}

class Day20 {

    fun run(fileName: String) {
        val maze = createData(fileName)
        val path = maze.solve()
        println("final result ${findShortCut(path, Point2D(maze.map[0].length, maze.map.size))}")
        println("final result ${findShortCut2(path, Point2D(maze.map[0].length, maze.map.size))}")
    }

    private fun findShortCut(path: Map<Point2D, Long>, size: Point2D): Long {
        var res = 0L
        path.forEach { (key, value) ->
            getNext2Dist(key)
                .filter { inRange(it, size) }
                .forEach {
                    if (path.contains(it)) {
                        val currentVal = path[it]!!
                        if (currentVal - value - 2 >= 100L) {
                            res++
                        }
                    }
                }
        }
        return res
    }

    private fun findShortCut2(path: Map<Point2D, Long>, size: Point2D): Long {
        var res = 0L
        val nextV2 = getNext2Dist2()
        val result = mutableMapOf<Long, Int>()
        path.forEach { (key, value) ->
            nextV2.filter { inRange(key + it, size) }
                .forEach {
                    if (path.contains(key+it)) {
                        val currentVal = path[key+it]!!
                        val currentDist = currentVal - value - getNbCase(it)
                        if (currentDist >= 100) {
                            if (!result.contains(currentDist)) {
                                result[currentDist] = 0
                            }
                            result[currentDist] = result[currentDist]!! + 1
                            res++
                        }
                    }
                }
        }
        return res
    }

    // size - (value + size - nextVal) => size - value - size +nextVal => nextVal -value
    private fun getNext2Dist(point: Point2D): List<Point2D> {
        return listOf(point + Point2D(0, 2), point + Point2D(0, -2), point + Point2D(-2, 0), point + Point2D(2, 0))
    }

    private fun getNbCase(point: Point2D): Int {
        return abs(point.x) + abs(point.y)
    }

    private fun getNext2Dist2(): Set<Point2D> {
        val points = mutableSetOf<Point2D>()
        for (i in 0..20) {
            for (j in 0..20 - i) {
                points.add(Point2D(i, j))
                points.add(Point2D(i, -j))
                points.add(Point2D(-i, j))
                points.add(Point2D(-i, -j))
            }
        }
        return points
    }

    private fun inRange(point: Point2D, size: Point2D): Boolean {
        return point.x >= 0 && point.x < size.x && point.y >= 0 && point.y < size.y
    }

    private fun createData(fileName: String): Maze20 {
        val bufferedReader: BufferedReader = File(fileName).bufferedReader()
        val maze = mutableListOf<String>()
        var start = Point2D(0, 0)
        var end = Point2D(0, 0)
        var y = 0
        bufferedReader.use {
            it.forEachLine { line: String ->
                run {
                    if (line.contains('E')) {
                        end = Point2D(line.indexOf('E'), y)
                    }
                    if (line.contains('S')) {
                        start = Point2D(line.indexOf('S'), y)
                    }
                    y++
                    maze.add(line)
                }
            }
        }
        bufferedReader.close()
        return Maze20(start, end, maze)
    }
}

fun main(args: Array<String>) {
    println("Running the main function");
    Day20().run("resource/day20.txt")
}

// 581281 too low
// 982124
// 1024383 too high
//