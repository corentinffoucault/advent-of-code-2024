import java.io.BufferedReader
import java.io.File
import kotlin.math.abs

data class KeySequence(val x1: Int, val x2: Int, val x3: Int, val x4: Int) {

    override fun toString(): String {
        return "$x1,$x2,$x3,$x4"
    }
}

class Day22 {

    private val sequenceToValues = mutableMapOf<KeySequence, MutableList<Int>>()

    fun run(fileName: String) {
        var fullResult = 0L
        val nbLoop = 2000
        val bufferedReader: BufferedReader = File(fileName).bufferedReader();
        bufferedReader.use {
            it.forEachLine {
                line: String -> run {
                    val tmp = calculateInLoop(line.toLong(), nbLoop)
                    fullResult += tmp
                }
            }
        }

        println(findBestSequence())

    }

    private fun findBestSequence(): Int {
        var maximum = 0
        sequenceToValues.forEach {
            maximum = maximum.coerceAtLeast(it.value.sum())
        }
        return maximum
    }

    private fun calculateInLoop(initialSecret: Long, nbLoop: Int): Long {
        var currentSecret = initialSecret
        var x0: Int
        var x1 = 0
        var x2 = 0
        var x3 = 0
        var x4 = initialSecret.mod(10)
        var alreadySeen = mutableSetOf<KeySequence>()

        for (i in 1..4) {
            currentSecret = calculateNextvalue(currentSecret)
            x1 = x2
            x2 = x3
            x3 = x4
            x4 = currentSecret.mod(10)
        }

        for (i in 5..nbLoop) {
            currentSecret = calculateNextvalue(currentSecret)
            x0 = x1
            x1 = x2
            x2 = x3
            x3 = x4
            x4 = currentSecret.mod(10)
            val currentSequence = KeySequence(x1-x0,x2-x1,x3-x2,x4-x3)
            if (!alreadySeen.contains(currentSequence)) {
                if (!sequenceToValues.contains(currentSequence)) {
                    sequenceToValues[currentSequence] = mutableListOf()
                }
                sequenceToValues[currentSequence]!!.add(x4)
                alreadySeen.add(currentSequence)
            }

        }
        return currentSecret
    }

    /*
    In particular, each buyer's secret number evolves into the next secret number in the sequence via the following process:

    Calculate the result of multiplying the secret number by 64. Then, mix this result into the secret number. Finally, prune the secret number.
    Calculate the result of dividing the secret number by 32. Round the result down to the nearest integer. Then, mix this result into the secret number. Finally, prune the secret number.
    Calculate the result of multiplying the secret number by 2048. Then, mix this result into the secret number. Finally, prune the secret number.

Each step of the above process involves mixing and pruning:

    To mix a value into the secret number, calculate the bitwise XOR of the given value and the secret number. Then, the secret number becomes the result of that operation. (If the secret number is 42 and you were to mix 15 into the secret number, the secret number would become 37.)
    To prune the secret number, calculate the value of the secret number modulo 16777216. Then, the secret number becomes the result of that operation. (If the secret number is 100000000 and you were to prune the secret number, the secret number would become 16113920.)

     */
    private fun mix(currentSecret: Long, initialSecret: Long): Long {
        return currentSecret xor initialSecret
    }

    private fun prune(currentSecret: Long): Long {
        return currentSecret.mod(16777216).toLong()
    }

    private fun calculateNextvalue(currentSecret: Long): Long {
        var tmpSecret = prune(mix(currentSecret*64, currentSecret))
        tmpSecret = prune(mix((tmpSecret).floorDiv(32), tmpSecret))
        return prune(mix(tmpSecret*2048, tmpSecret))
    }
}

fun main(args: Array<String>) {
    println("Running the main function");
    Day22().run("resource/day22.txt");
}