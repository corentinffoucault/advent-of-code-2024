import java.io.BufferedReader
import java.io.File
import kotlin.math.abs
import kotlin.math.max

class Machine(private val buttonA: Point2D, private val buttonB: Point2D, private val prize: Point2D) {
    fun solveWithCramer(factor: Long = 0L): Long {
        val prizeXBig = prize.x +factor
        val prizeYBig = prize.y +factor
        val det = buttonA.x * buttonB.y - buttonA.y * buttonB.x
        val a = (prizeXBig * buttonB.y - prizeYBig * buttonB.x) / det
        val b = (buttonA.x * prizeYBig - buttonA.y * prizeXBig) / det
        return if (buttonA.x * a + buttonB.x * b == prizeXBig && buttonA.y * a + buttonB.y * b == prizeYBig) {
            a * 3L + b
        } else 0L
    }
}

class Day13 {

    fun run(fileName: String) {
        val machine = createData(fileName)
        println("final result ${machine.fold(0L) {acc, it -> acc+ it.solveWithCramer() }}")
        println("final result ${machine.fold(0L) {acc, it -> acc+ it.solveWithCramer(10000000000000L) }}")
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