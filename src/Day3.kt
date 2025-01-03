import java.io.BufferedReader
import java.io.File
import kotlin.math.abs

class Day3 {

    fun run(fileName: String) {
        var result = 0
        val bufferedReader: BufferedReader = File(fileName).bufferedReader();
        var mulEnabled = true
        bufferedReader.use {
            it.forEachLine {
                line: String -> run {
                    val tmp = analyzeLine(line, mulEnabled)
                    result += tmp.first
                    mulEnabled = tmp.second
                }
            }
        };
        println(result)
    }

    private fun analyzeLine(line: String, _mulEnabled: Boolean): Pair<Int,Boolean> {
        val reg = Regex("mul\\(([0-9]{0,3}),([0-9]{0,3})\\)|do\\(\\)|don't\\(\\)")
        var mulEnabled = _mulEnabled
        var result = 0
        reg.findAll(line).forEach {
            item -> run {
                if (item.value == "do()") {
                    mulEnabled = true
                    return@forEach
                }
                if (item.value == "don't()") {
                    mulEnabled = false
                    return@forEach
                }
                if (mulEnabled) {
                    result += item.groups[2]?.value?.toInt()?.let { item.groups[1]?.value?.toInt()?.times(it) } ?: 0
                }
            }
        }

        return Pair(result,mulEnabled)
    }
}

fun main(args: Array<String>) {
    println("Running the main function");
    Day3().run("resource/day3.txt");
}