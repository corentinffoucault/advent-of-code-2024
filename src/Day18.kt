import java.io.BufferedReader
import java.io.File
import java.util.*
import kotlin.math.abs
import kotlin.math.floor
import kotlin.math.pow

class Maze18(private val start: Point2D, private val end: Point2D, val size: Point2D) {

    private fun inRange(point: Point2D): Boolean {
        return point.x >= 0 && point.x < size.x && point.y >= 0 && point.y < size.y
    }

    fun solve(corrupted: Set<Point2D>): Int {
        val queue = mutableListOf(start to 0)
        val seen = mutableSetOf<Point2D>()

        while (queue.isNotEmpty()) {
            val (currentPoint, cost) = queue.removeFirst()

            if (currentPoint == end) return cost
            else if (seen.add(currentPoint)) {

                getNext(currentPoint)
                    .filter { inRange(it) && !corrupted.contains(it) }
                    .forEach { queue.add(it to cost + 1) }
            }
        }
        return -1
    }

    fun solve2(corrupted: List<Point2D>): String {
        val index = dicotomie(corrupted.toList())!!
        val point = corrupted.toList()[index]
        return  "${point.x},${point.y}"
    }

    private fun dicotomie(list: List<Point2D>): Int? {
        var min = 0
        var max = list.count()
        var firstFind: Int? = null
        while (min <= max) {
            val mid = (min + max)/2
            if (solve(list.take(mid + 1).toSet()) == -1) {
                firstFind = mid
                max = mid - 1
            } else {
                min = mid + 1
            }
        }
        return firstFind
    }

    private fun getNext(point: Point2D): List<Point2D> {
        return listOf(point+Point2D(0,1), point+Point2D(0,-1), point+Point2D(-1,0), point+Point2D(1,0))
    }
}

class Day18 {

    fun run(fileName: String) {
        val (computer, corrupted) = createData(fileName)
        println("final result ${computer.solve(corrupted.take(1024).toSet()) }")
    }
    fun run2(fileName: String) {
        val (computer, corrupted) = createData(fileName)
        println("final result ${computer.solve2(corrupted) }")
    }

    private fun createData(fileName: String): Pair<Maze18, List<Point2D>> {
        val bufferedReader: BufferedReader = File(fileName).bufferedReader()
        val corrupted = mutableListOf<Point2D>()
        bufferedReader.use {
            it.forEachLine { line: String ->
                run {
                    val elem = line.split(",")
                    corrupted.add(Point2D.fromString(elem[0], elem[1]))
                }
            }
        }
        bufferedReader.close()
        return Pair(Maze18(Point2D(0,0), Point2D(70,70), Point2D(71,71)), corrupted)
    }
}

fun main(args: Array<String>) {
    println("Running the main function");
    Day18().run("resource/day18.txt")
    Day18().run2("resource/day18.txt")
}
