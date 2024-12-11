import java.io.BufferedReader
import java.io.File

class Topographic(var zeros: Array<Point2D>, var map: Array<Array<Int>>) {
    fun findAllFullPathFromAllSource(): Int {
        return zeros.fold(0) { acc, it ->  acc + findAllPathFromOnSource(it) }
    }

    fun findAllFullPathFromAllSourceV2(): Int {
        return zeros.fold(0) { acc, it ->  acc + findAllPathFromOnSourceV2(it) }
    }

    private fun findAllPathFromOnSource(zero: Point2D): Int {
        var currentPoints = setOf(zero)
        var seen: Set<Point2D> = emptySet()
        var index = 0
        while (index < 9) {
            seen = emptySet()
            currentPoints.forEach {
                seen = seen union getAllNext(it).toSet()
            }
            index++
            currentPoints = seen
        }
        return seen.size
    }

    private fun findAllPathFromOnSourceV2(zero: Point2D): Int {
        var currentPoints = listOf(zero)
        var nextPoints = emptyList<Point2D>()
        var index = 0
        while (index < 9) {
            nextPoints = emptyList()
            currentPoints.forEach {
                nextPoints = nextPoints + getAllNext(it)
            }
            index++
            currentPoints = nextPoints
        }
        return nextPoints.size
    }

    fun getAllNext(point: Point2D): Array<Point2D> {
        val value = map[point.y][point.x]
        val nextPoints = mutableListOf<Point2D>()

        if (point.x > 0 && map[point.y][point.x-1] == value+1) {
            nextPoints.add(Point2D(point.x-1, point.y))
        }
        if (point.x < map[0].size-1 && map[point.y][point.x+1] == value+1) {
            nextPoints.add(Point2D(point.x+1, point.y))
        }
        if (point.y > 0 && map[point.y-1][point.x] == value+1) {
            nextPoints.add(Point2D(point.x, point.y-1))
        }
        if (point.y < map.size-1 && map[point.y+1][point.x] == value+1) {
            nextPoints.add(Point2D(point.x, point.y+1))
        }

        return nextPoints.toTypedArray()
    }
}

class Day10 {

    fun run(fileName: String) {
        val topo = createData(fileName)
        println(topo.findAllFullPathFromAllSource())
        println(topo.findAllFullPathFromAllSourceV2())
    }

    private fun createData(fileName: String): Topographic {
        val bufferedReader: BufferedReader = File(fileName).bufferedReader()
        val map: MutableList<Array<Int>> = mutableListOf()
        val zeros: MutableList<Point2D> = mutableListOf()
        var y = 0
        bufferedReader.use {
            it.forEachLine { line: String ->
                run {
                    val splittedLine = line.map { value -> run { value.digitToInt() } }.toTypedArray()
                    map.add(splittedLine)
                    splittedLine.forEachIndexed { x, item -> run {
                        if (item == 0) {
                            zeros.add(Point2D(x, y))
                        }
                    } }
                }
                y++
            }
        }
        return Topographic(zeros.toTypedArray(), map.toTypedArray())
    }
}


fun main(args: Array<String>) {
    println("Running the main function");
    Day10().run("resource/day10.txt")
}