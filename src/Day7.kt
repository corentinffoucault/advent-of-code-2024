import java.io.BufferedReader
import java.io.File

class Equation() {
    var result = 0L
    lateinit var params: Array<Long>

    constructor(line: String) : this() {
        val splitedine = line.split(':')
        result = splitedine[0].toLong()
        params = splitedine[1].trimStart().split(' ').map { it.toLong() }.toTypedArray()
    }

    fun solve(): Long {
        if (params.size == 1) {
            return  if (result == params.first()) result else 0L
        }

        if(solveLoop(params[0], 1)) {
            return result
        }
        return 0L
    }

    fun solveV2(): Long {
        if (params.size == 1) {
            return  if (result == params.first()) result else 0L
        }

        if(solveLoop(params[0], 1, true)) {
            return result
        }
        return 0L
    }

    fun solveLoop(currentRes: Long, currentIndex: Int, useConcat: Boolean = false): Boolean {
        val mul = currentRes * params[currentIndex]
        val sum = currentRes + params[currentIndex]
        var concat = 0L

        if (useConcat) {
            concat = (currentRes.toString() + params[currentIndex].toString()).toLong()
        }

        if (currentIndex == params.size-1) {
            return mul == result || sum == result || concat == result
        }

        var tmp = false
        if (mul <= result) {
            tmp = tmp || solveLoop(mul, currentIndex+1, useConcat)
        }
        if (sum <= result) {
            tmp = tmp || solveLoop(sum, currentIndex+1, useConcat)
        }
        if (useConcat && concat <= result) {
            tmp = tmp || solveLoop(concat, currentIndex+1, true)
        }

        return tmp
    }

}

class Day7 {

    fun run(fileName: String) {
        val equations = createData(fileName);
        var result = 0L
        equations.forEach { result += it.solve() }
        println("corrected Result ${result}")
        result = 0L
        equations.forEach { result += it.solveV2() }
        println("corrected Result V2 ${result}")
    }

    private fun createData(fileName: String): Array<Equation> {
        val bufferedReader: BufferedReader = File(fileName).bufferedReader();
        val equations: MutableList<Equation> = mutableListOf()
        bufferedReader.use {
            it.forEachLine { line: String ->
                run {
                    equations.add(Equation(line))
                }
            }
        }
        return equations.toTypedArray()
    }

}


fun main(args: Array<String>) {
    println("Running the main function");
    Day7().run("resource/day7.txt");
}