import java.io.BufferedReader
import java.io.File
import kotlin.math.abs

class MazeSolver(private val start: Point2D, private val end: Point2D, private val maze: List<String>, private val initialDir: Point2D = Point2D(1,0)) {
    fun solve(): Long {
        println("start $start end $end")
        maze.forEach{ println(it) }
        return 0L
    }
}

class Day16 {

    fun run(fileName: String) {
        val mazeSolver = createData(fileName)
        println("final result ${mazeSolver.solve() }}")
    }

    private fun createData(fileName: String): MazeSolver {
        val bufferedReader: BufferedReader = File(fileName).bufferedReader()
        var maze = mutableListOf<String>()
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
                    maze.add(line)
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