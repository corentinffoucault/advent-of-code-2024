import java.io.BufferedReader
import java.io.File

data class TowelPart(val currentVal: Char, val nextVals: MutableMap<Char, TowelPart> = mutableMapOf(), var isFinal: Boolean = false) {
    fun getPossibleTowelRemovingTowelPart(towel: String): List<String> {
        val reminder = mutableListOf<String>()
        if (isFinal) {
            reminder.add(towel)
        }
        if (towel.isEmpty()) {
            return reminder
        }
        val firstChar = towel[0]
        if (currentVal == ' ' && !nextVals.contains(firstChar)) {
            println(towel)
        }
        if (nextVals.contains(firstChar)) {
            reminder.addAll(nextVals[firstChar]!!.getPossibleTowelRemovingTowelPart(towel.substring(1)))
        }
        return reminder
    }

    fun addTowelPart(parts: String) {
        if (parts.isEmpty()) {
            isFinal = true
            return
        }
        val firstChar = parts[0]
        if (!nextVals.contains(firstChar)) {
            nextVals[firstChar] = TowelPart(firstChar)
        }
        nextVals[firstChar]!!.addTowelPart(parts.substring(1))
    }
}

class TowelsAnalyser(private val towels: List<String>, private val towelParts: TowelPart) {

    var seen = mutableMapOf<String, Long>()

    fun solve(): Long {
        var nbValid = 0L
        var nbValidBis = 0
        towels.forEach { towel -> run {
                seen = mutableMapOf<String, Long>()
                val currentPos = isValid(listOf(towel))
                if (currentPos>0){
                    nbValid+=currentPos
                    nbValidBis++
                    println("$towel")
                } else {
                //    println("$towel")
                }
        } }
        println(nbValidBis)
        return nbValid
    }

    private fun isValid(towels: List<String>): Long {
        if (towels.isEmpty()) {
            return 0
        }
        var currentRes = 0L
        towels.forEach { subTowel ->
            if (subTowel.isEmpty()) {
                currentRes += 1
            }
            if (!seen.contains(subTowel)) {
                val res = towelParts.getPossibleTowelRemovingTowelPart(subTowel)
                val isValidRes = isValid(res)
                seen[subTowel] = isValidRes
                currentRes += isValidRes
            } else {
                println(seen[subTowel])
                currentRes += seen[subTowel]!!
            }
        }
        return currentRes
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
        val towelParts = iterator.next().split(",").fold(TowelPart(' ')) { acc, it -> run {
            acc.addTowelPart(it.trim())
            return@fold acc
        } }
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

// 9444791 too low