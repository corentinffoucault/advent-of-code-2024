import java.io.BufferedReader
import java.io.File
import java.util.*
import kotlin.math.abs
import kotlin.math.pow

class TowelsAnalyser(private val towels: List<String>, private val towelParts: Set<String>) {
    fun solve(): Int {
        return towels.fold(0) { acc, it -> run {
                return acc+1
        } }
    }
}

class Day19 {

    fun run(fileName: String) {
        val towelsAnalyser = createData(fileName)
        println("final result ${towelsAnalyser.solve()}")
    }

    private fun createData(fileName: String): TowelsAnalyser {
        val bufferedReader: BufferedReader = File(fileName).bufferedReader()
        val towels = mutableListOf<String>()
        val iterator = bufferedReader.lineSequence().iterator()
        val towelParts = iterator.next().split(",").toSet()
        iterator.next()
        while(iterator.hasNext()) {
            towels.add(iterator.next())
        }
        bufferedReader.close()
        return TowelsAnalyser(towels, towelParts)
    }
}

fun main(args: Array<String>) {
    println("Running the main function");
    Day19().run("resource/day19.txt")
}
