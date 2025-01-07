import java.io.BufferedReader
import java.io.File

data class KeyOrLock(val value: List<Int>) {
    companion object {
        operator fun invoke(spaceSeparated: List<String>): KeyOrLock {
            val info = spaceSeparated.fold(mutableListOf(0, 0, 0, 0, 0)) { acc, it ->
                it.forEachIndexed { index, it ->
                    acc[index]+= if (it=='#') 1 else 0
                }
                acc
            }
            return KeyOrLock(info)
        }
    }

    operator fun plus(other: KeyOrLock): KeyOrLock =
        KeyOrLock(value.mapIndexed { index,it->it+other.value[index] })

    fun areComplement(other: KeyOrLock): Boolean {
        val fusion = this + other
        fusion.value.forEach {
            if (it>7) {
                return false
            }
        }
        return true
    }
}

class Day25 {

    val keys = mutableSetOf<KeyOrLock>()
    val locks = mutableSetOf<KeyOrLock>()

    fun run(fileName: String) {
        extractInfo(fileName)
        solveV1()
    }

    fun solveV1() {
        val nbCouple = keys.fold(0) { acc, key ->
            acc + locks.fold(0) { subAcc, lock ->
                subAcc+ (if (key.areComplement(lock)) 1 else 0)
            }
        }

        println("nbKeys: ${keys.size} nbLock: ${locks.size} nbCouple: $nbCouple")
    }

    private fun extractInfo(fileName: String) {
        val bufferedReader: BufferedReader = File(fileName).bufferedReader();

        val iterator = bufferedReader.lineSequence().iterator()
        var block = mutableListOf<String>()
        while (iterator.hasNext()) {
            val line = iterator.next()
            if (line.isNotEmpty()) {
                block.add(line)
                if (block.size == 7) {
                    if (line[0] == '#') {
                        keys.add(KeyOrLock(block))
                    } else {
                        locks.add(KeyOrLock(block))
                    }
                    block = mutableListOf()
                }
            }
        }
        bufferedReader.close()
    }
}

fun main(args: Array<String>) {
    println("Running the main function");
    Day25().run("resource/day25.txt");
}

// 15854 too high