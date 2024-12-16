import java.io.BufferedReader
import java.io.File
import kotlin.math.abs

class Machine(private val buttonA: Point2D, private val buttonB: Point2D, private val prize: Point2D) {
    fun solve(): Long {
        val valid = false
        val nbButtonAPress = 0L
        val nbButtonBPress = 0L
        println("ButtonA $buttonA ButtonB $buttonB Prize $prize")
        return if (valid) nbButtonAPress*3L + nbButtonBPress else 0L
    }
}

class Day13 {

    fun run(fileName: String) {
        val machine = createData(fileName)
        println("final result ${machine.fold(0L) {acc, it -> acc+ it.solve() }}")
    }

    private fun createData(fileName: String): List<Machine> {
        val bufferedReader: BufferedReader = File(fileName).bufferedReader()
        val machines = mutableListOf<Machine>()
        var nbLine = 0;
        var currentA = Point2D(0, 0)
        var currentB = Point2D(0, 0)
        bufferedReader.use {
            it.forEachLine { line: String ->
                run {
                    if (nbLine%4 == 0) {
                        val reg = Regex("Button A: X\\+([0-9]+), Y\\+([0-9]+)")
                        val res = reg.find(line)
                        val x = res?.groups?.get(1)?.value?.toInt()
                        val y = res?.groups?.get(2)?.value?.toInt()
                        if (x != null && y != null) {
                            currentA = Point2D(x, y)
                        }
                    } else if (nbLine%4 == 1) {
                        val reg = Regex("Button B: X\\+([0-9]+), Y\\+([0-9]+)")
                        val res = reg.find(line)
                        val x = res?.groups?.get(1)?.value?.toInt()
                        val y = res?.groups?.get(2)?.value?.toInt()
                        if (x != null && y != null) {
                            currentB = Point2D(x, y)
                        }
                    } else if (nbLine%4 == 2) {
                        val reg = Regex("Prize: X=([0-9]+), Y=([0-9]+)")
                        val res = reg.find(line)
                        val x = res?.groups?.get(1)?.value?.toInt()
                        val y = res?.groups?.get(2)?.value?.toInt()
                        if (x != null && y != null) {
                            machines.add(Machine(currentA, currentB, Point2D(x, y)))
                        }
                    }
                }
                nbLine++
            }
        }
        return machines
    }
}

fun main(args: Array<String>) {
    println("Running the main function");
    Day13().run("resource/day13.txt")
}