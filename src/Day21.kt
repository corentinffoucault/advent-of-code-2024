import java.io.BufferedReader
import java.io.File
import kotlin.math.abs

class Robot21(
    private val padNum: Map<Char, Point2D>,
    private val padDir: Map<Char, Point2D>,
    private val debug: Boolean = false) {

    private val possibility = mutableMapOf<Pair<Char, Char>, List<String>>()
    private var seen = mutableMapOf<Pair<Pair<Char, Char>, Int>, Long>()

    fun solve(code: String, depth: Int): String {
        return getListCommand(code, depth, padNum, Point2D(0, 0))
    }

    fun solveV2(code: String, depth: Int): Long {
        return getListCommandV2(code, depth, padNum, Point2D(0, 0))
    }

    private fun getListCommand(listCommand: String, depth: Int, pad: Map<Char, Point2D>, invalidPoint: Point2D): String {
        var previousVal = 'A'
        var currentPosition = pad[previousVal]!!
        return listCommand.fold(StringBuilder()) { acc, it ->
            val possibleMovement = getListCommandForOneChar(it, pad, currentPosition)
            currentPosition = pad[it]!!
            if (!possibility.contains(previousVal to it)) {
                val allValidSubListCommand = cleanSublistCommand(possibleMovement, invalidPoint, pad[previousVal]!!)
                possibility[previousVal to it] = allValidSubListCommand
            }
            val tmp = getMinimalAlternative(possibility[previousVal to it]!!, depth)
            if (debug) {
                println("before $previousVal next $it depth $depth value $tmp ")
            }
            acc.append(tmp)
            previousVal = it
            return@fold acc
        }.toString()
    }

    private fun getListCommandV2(listCommand: String, depth: Int, pad: Map<Char, Point2D>, invalidPoint: Point2D): Long {
        var previousVal = 'A'
        var currentPosition = pad[previousVal]!!
        return listCommand.fold(0L) { acc, it ->
            if (!seen.contains(Pair(Pair(previousVal, it), depth))) {
                val possibleMovement = getListCommandForOneChar(it, pad, currentPosition)
                if (!possibility.contains(previousVal to it)) {
                    possibility[previousVal to it] = cleanSublistCommand(possibleMovement, invalidPoint, pad[previousVal]!!)
                }
                seen[Pair(Pair(previousVal, it), depth)] = getMinimalAlternativeV2(possibility[previousVal to it]!!, depth)
            }
            currentPosition = pad[it]!!
            val tmp = seen[Pair(Pair(previousVal, it), depth)]!!
            previousVal = it
            return@fold acc+tmp
        }
    }

    private fun getMinimalAlternative(allValidSubListCommand: List<String>, depth: Int): String {
        return allValidSubListCommand.fold(Pair("", Int.MAX_VALUE)) { minCommand, currentCommand ->
            var tmpValue = currentCommand
            if (depth != 0) {
                tmpValue = getListCommand(currentCommand, depth - 1, padDir, Point2D(0, 1))
            }
            if (tmpValue.length < minCommand.second) {
                return@fold Pair(tmpValue, tmpValue.length)
            }
            return@fold minCommand
        }.first
    }

    private fun getMinimalAlternativeV2(allValidSubListCommand: List<String>, depth: Int): Long {
        return allValidSubListCommand.fold(Long.MAX_VALUE) { minSize, currentCommand ->
            var tmpValue = currentCommand.length.toLong()
            if (depth != 0) {
                tmpValue = getListCommandV2(currentCommand, depth - 1, padDir, Point2D(0, 1))
            }
            if (tmpValue < minSize) {
                return@fold tmpValue
            }
            return@fold minSize
        }
    }

    private fun String.permute(result: String = ""): List<String> =
        if (isEmpty()) listOf(result) else flatMapIndexed { i, c -> removeRange(i, i + 1).permute(result + c) }


    private fun cleanSublistCommand(subListCommand: String, invalidPoint: Point2D, previousPos: Point2D): List<String> {
        val a: List<String> = subListCommand.permute()
        return a.fold(mutableSetOf<String>()) { acc, command ->
            var currentPosition = previousPos
            command.forEach {
                currentPosition += when (it) {
                    '<' -> Point2D.WEST
                    '>' -> Point2D.EAST
                    '^' -> Point2D.SOUTH
                    'v' -> Point2D.NORTH
                    else -> Point2D.ORIGIN
                }
                if (currentPosition==invalidPoint) {
                    return@fold acc
                }
            }
            acc.add(command+"A")
            return@fold acc
        }.toList()
    }

    private fun getListCommandForOneChar(value: Char, pad: Map<Char, Point2D>, position: Point2D): String {
        val nextPosition = pad[value]!!
        if (debug && value=='>') {
            print("before $position next $nextPosition value $value ")
        }
        val diff = position - nextPosition
        val result = StringBuilder()
        if (diff.x > 0) {
            result.append("<".repeat(diff.x))
        }

        if (diff.y > 0) {
            result.append("v".repeat(diff.y))
        }

        if (diff.x < 0) {
            result.append(">".repeat(abs(diff.x)))
        }

        if (diff.y < 0) {
            result.append("^".repeat(abs(diff.y)))
        }

        if (debug) {
            println("result $result")
        }
        return result.toString()
    }
}

class Day21 {
    private val numPad = mapOf(
        '0' to Point2D(1, 0),
        'A' to Point2D(2, 0),
        '1' to Point2D(0, 1),
        '2' to Point2D(1, 1),
        '3' to Point2D(2, 1),
        '4' to Point2D(0, 2),
        '5' to Point2D(1, 2),
        '6' to Point2D(2, 2),
        '7' to Point2D(0, 3),
        '8' to Point2D(1, 3),
        '9' to Point2D(2, 3),
    )

    private val dirPad = mapOf(
        '<' to Point2D(0, 0),
        'v' to Point2D(1, 0),
        '>' to Point2D(2, 0),
        '^' to Point2D(1, 1),
        'A' to Point2D(2, 1),
    )

    fun run(fileName: String) {
        val codes = createData(fileName)
        val spaceRobot = Robot21(numPad, dirPad)
        var result = 0L;
        codes.forEach {
            val commandForSpace = spaceRobot.solve(it, 2)
            println("code: $it => command for space: $commandForSpace => ${commandForSpace.length}")
            result += commandForSpace.length * it.replace("[^0-9]".toRegex(), "").toLong()
        }

        println(result)
    }

    fun run2(fileName: String) {
        val codes = createData(fileName)
        val spaceRobot = Robot21(numPad, dirPad)
        var result = 0L;
        codes.forEach {
            val commandForSpace = spaceRobot.solveV2(it, 25)
            println("code: $it => command for space: $commandForSpace")
            result += commandForSpace * it.replace("[^0-9]".toRegex(), "").toLong()
        }
        println(result)
    }


    private fun createData(fileName: String): List<String> {
        val bufferedReader: BufferedReader = File(fileName).bufferedReader()
        val codes = mutableListOf<String>()
        bufferedReader.use {
            it.forEachLine { line: String -> codes.add(line) }
        }
        bufferedReader.close()
        return codes
    }
}

fun main(args: Array<String>) {
    println("Running the main function");
    Day21().run("resource/day21.txt")
    Day21().run2("resource/day21.txt")
}