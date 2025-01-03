import java.io.BufferedReader
import java.io.File

private data class Spot(val coord: Point2D, val dir: Point2D) {
    var score = Long.MAX_VALUE
    var spotBefore = mutableSetOf<Point2D>()

    fun getKey(): Pair<Point2D, Point2D> {
        return coord to dir
    }
}

class MazeSolver(private val start: Point2D, private val end: Point2D, private val maze: List<CharArray>, private val initialDir: Point2D = Point2D(1,0)) {

    val size = Point2D(maze[0].size, maze.size)

    private fun inRange(point: Point2D): Boolean {
        return point.x >= 0 && point.x < size.x && point.y >= 0 && point.y < size.y
    }

    fun solve(): Long {
        val initialSpot = Spot(start, initialDir)
        initialSpot.score = 1
        initialSpot.spotBefore.add(start)
        val queue = mutableListOf(initialSpot)
        val seen = mutableMapOf<Pair<Point2D,Point2D>, Spot>()
        var min = Spot(Point2D(-1, -1), Point2D(-1, -1))
        while (queue.isNotEmpty()) {
            val currentSpot = queue.removeFirst()
            var goToNext = false
            if (currentSpot.coord == end) {
                if (min.score>currentSpot.score) {
                    min = currentSpot
                } else if (min.score==currentSpot.score){
                    min.spotBefore += currentSpot.spotBefore
                }
            } else {
                val key = currentSpot.getKey()
                if (!seen.contains(key)) {
                    goToNext = true
                    seen[key] = currentSpot
                } else if (seen[key]!!.score > currentSpot.score) {
                    goToNext = true
                    seen[key] = currentSpot
                } else if (seen[key]!!.score == currentSpot.score) {
                    goToNext = true
                    seen[key]!!.spotBefore += currentSpot.spotBefore
                }
                if (goToNext) {
                    getNext(currentSpot.coord)
                        .filter { inRange(it) && maze[it.y][it.x] != '#' }
                        .forEach {
                            val nextDir = currentSpot.coord - it
                            val currentCost = getCost(currentSpot.dir, nextDir)
                            val nextSpot = Spot(it, nextDir)
                            nextSpot.score = currentSpot.score + currentCost
                            nextSpot.spotBefore = currentSpot.spotBefore.toMutableSet()
                            nextSpot.spotBefore.add(it)
                            queue.add(nextSpot)
                        }
                }
            }
        }
        println(min.spotBefore.size)
        return min.score
    }

    private fun getCost(currentDirection: Point2D, nextDirection: Point2D): Long {
        if (currentDirection==nextDirection) {
            return 1
        }
        return 1001
    }

    private fun getNext(point: Point2D): List<Point2D> {
        return listOf(point+Point2D(0,1), point+Point2D(0,-1), point+Point2D(-1,0), point+Point2D(1,0))
    }
}

class Day16 {

    fun run(fileName: String) {
        val mazeSolver = createData(fileName)
        println("final result ${mazeSolver.solve() }")
    }

    private fun createData(fileName: String): MazeSolver {
        val bufferedReader: BufferedReader = File(fileName).bufferedReader()
        var maze = mutableListOf<CharArray>()
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
                    maze.add(line.toCharArray())
                    y++
                }
            }
        }
        return MazeSolver(start, end, maze)
    }
}

fun main(args: Array<String>) {
    println("Running the main function");
    Day16().run("resource/day16.txt")
}