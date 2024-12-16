import java.io.BufferedReader
import java.io.File
import kotlin.math.abs

class Robot(private val initialPoint: Point2D, private val speed: Point2D) {
    fun solve(): Long {
        println("initialPos $initialPoint speed $speed")
        return 0L
    }
}

class Day14 {

    fun run(fileName: String) {
        val machine = createData(fileName)
        println("final result ${machine.fold(0L) {acc, it -> acc+ it.solve() }}")
    }

    private fun createData(fileName: String): List<Robot> {
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
                        robots.add(Robot(Point2D(x0, y0), Point2D(cx, cy)))
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
}