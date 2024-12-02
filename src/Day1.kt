import java.io.BufferedReader
import java.io.File

class Day1 {

    fun run(fileName: String) {
        val bufferedReader: BufferedReader = File(fileName).bufferedReader();
        val inputString = bufferedReader.use { it.readText() };
        println(inputString);
    }
}

fun main() {
    println("Running the main function");
    Day1().run("resource/day1.txt");
}