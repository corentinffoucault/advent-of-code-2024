import java.io.BufferedReader
import java.io.File

class Day1 {

    fun run(fileName: String) {
        val part1 = mutableListOf<Int>()
        val part2 = mutableListOf<Int>()

        val bufferedReader: BufferedReader = File(fileName).bufferedReader();
        bufferedReader.use {
            it.forEachLine {
                    s: String -> run {
                    val line = s.split(" ")
                    part1.add(line[0].toInt())
                    part2.add(line[3].toInt())
                }
            }
        };
        part1.sort()
        part2.sort()
        var result = 0
        part1.forEachIndexed{index, item -> run {
            result += item * part2.count{it -> it == item}
        }
        }

        println(result)

    }

}

fun main(args: Array<String>) {
    println("Running the main function");
    Day1().run("resource/day1.txt");
}