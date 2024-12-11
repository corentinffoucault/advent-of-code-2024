import java.io.BufferedReader
import java.io.File

class Day11 {

    private var limit = 25;
    private var result = 0
    private var cache = mutableMapOf<Pair<Long, Int>, Long>()

    fun run(fileName: String) {
        val arrangement = createData(fileName)
        limit = 25
        result = 0
        cache = mutableMapOf()
        val result = arrangement.fold(0L) { acc, it -> acc + buildNextLevel(it, 0) }
        println(result)
    }
    fun run2(fileName: String) {
        val arrangement = createData(fileName)
        limit = 75
        result = 0
        cache = mutableMapOf()
        val result = arrangement.fold(0L) { acc, it -> acc + buildNextLevel(it, 0) }
        println(result)
    }

    private fun createData(fileName: String): List<Long> {
        val bufferedReader: BufferedReader = File(fileName).bufferedReader()
        return bufferedReader.readLine().split(" ").map { it.toLong() }
    }

    private fun buildNextLevel(currentValue: Long, indexLoop: Int): Long {
        if (indexLoop == limit) {
            return 1
        }
        if (Pair(currentValue, indexLoop) in cache) {
            return cache.getValue(Pair(currentValue, indexLoop))
        }
        val res: Long
        if (currentValue == 0L) {
            res = buildNextLevel(1L, indexLoop+1)
        } else {
            val stringValue = currentValue.toString()
            val size = stringValue.length
            if (size % 2 == 0) {
                val semiSize = size / 2
                res = buildNextLevel(stringValue.substring(0, semiSize).toLong(), indexLoop+1) +
                        buildNextLevel(stringValue.substring(semiSize).toLong(), indexLoop+1)
            } else {
                res = buildNextLevel(currentValue * 2024L, indexLoop+1)
            }
        }
        cache[Pair(currentValue, indexLoop)] = res
        return res
    }
}

fun main(args: Array<String>) {
    println("Running the main function");
    Day11().run("resource/day11.txt")
    Day11().run2("resource/day11.txt")
}