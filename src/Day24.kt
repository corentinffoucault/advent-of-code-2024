import java.io.BufferedReader
import java.io.File
import java.io.FileWriter


data class Door(val v1: String, val v2: String, val op: String, val resultName: String) {

    private var res= -1

    fun eval(informations: Map<String, Door>, initialEntry: Map<String, InitialEntry>): Int {
        if (res == -1) {
            val value1 = if (informations.contains(v1)) informations[v1]!!.eval(informations, initialEntry) else initialEntry[v1]!!.eval()
            val value2 = if (informations.contains(v2)) informations[v2]!!.eval(informations, initialEntry) else initialEntry[v2]!!.eval()
            res = when (op) {
                "XOR" -> if (value1 != value2) 1 else 0;
                "AND" -> if (value1 == 1 && value2 == 1) 1 else 0;
                "OR" -> if (value1 == 1 || value2 == 1) 1 else 0;
                else -> v1.toInt()
            }
        }
        return res
    }

    fun listInitialEntry(informations: Map<String, Door>): List<String> {
        val initialEntry = mutableListOf<String>()
        if (informations.contains(v1)) {
            initialEntry.addAll(informations[v1]!!.listInitialEntry(informations))
        } else {
            initialEntry.add(v1)
        }
        if (informations.contains(v2)) {
            initialEntry.addAll(informations[v2]!!.listInitialEntry(informations))
        } else {
            initialEntry.add(v2)
        }
        return initialEntry
    }

    override fun toString(): String {
        return res.toString()
    }
}

data class InitialEntry(private val result: Int) {
    fun eval(): Int {
        return result
    }
}

class Day24 {

    val informations = mutableMapOf<String, Door>()
    val initialEntry = mutableMapOf<String, InitialEntry>()

    fun run(fileName: String, outputFile: String) {
        extractInfo(fileName)
        println(evalAll())
        solvePart2(outputFile)
    }

    private fun evalAll(): Pair<Long, Map<String, List<String>>> {
        val zValue = mutableSetOf<String>()
        val ztoXY = mutableMapOf<String, List<String>>()
        informations.forEach{(key, value) ->
            value.eval(informations, initialEntry)
            if (key[0]=='z') {
                zValue.add(key)
            }
        }
        val result = zValue.sortedDescending().fold(StringBuilder()) { acc, it ->
            acc.append(informations[it]!!.eval(informations, initialEntry))
            ztoXY[it] = informations[it]!!.listInitialEntry(informations)
            acc
        }
        return result.toString().toLong(2) to ztoXY
    }

    fun solvePart2(outputFile: String) {
        val f = File(outputFile)
        f.parentFile.mkdir()
        val bufferedWriter= f.bufferedWriter();
        val (x, y) = initialEntry.keys.fold(mutableListOf<String>() to mutableListOf<String>()) { acc, it ->
            if(it.startsWith('x')) {
                acc.first.add(it)
            } else if(it.startsWith('y')) (
                acc.second.add(it)
            )
            acc
        }

        bufferedWriter.write(
            """
        digraph G {
            subgraph {
                node [style=filled,color=gray]
                ${x.joinToString("->")} [color="none"]
            }
            subgraph {
                node [style=filled,color=gray]
                ${y.joinToString("->")} [color="none"]
            }
            subgraph {
                node [style=filled,color=pink,shape=square]
                ${informations.filter { gate -> gate.value.op == "AND" }.values.joinToString(" ") { gate -> gate.resultName }}
            }
            subgraph {
                node [style=filled,color=yellow,shape=diamond];
                ${informations.filter { gate -> gate.value.op == "OR" }.values.joinToString(" ") { gate -> gate.resultName }}
            }
            subgraph {
                node [style=filled,color=lightblue,shape=house];
                ${informations.filter { gate -> gate.value.op == "XOR" }.values.joinToString(" ") { gate -> gate.resultName }}
            }
            """.trimIndent()
        )
        informations.forEach { (key, value) ->
            bufferedWriter.write("    ${value.v1} -> $key\n")
            bufferedWriter.write("    ${value.v2} -> $key\n")
        }
        bufferedWriter.write("}")
        bufferedWriter.close()
        // command to generateGraph dot -Tsvg day24.dot > output.svg
    }

    private fun extractInfo(fileName: String) {
        val bufferedReader: BufferedReader = File(fileName).bufferedReader();

        val iterator = bufferedReader.lineSequence().iterator()
        while (iterator.hasNext()) {
            val line = iterator.next()
            if (line.isEmpty()) {
                break
            }
            val entry = line.split(": ")
            initialEntry[entry[0]] = InitialEntry(entry[1].toInt())
        }

        while (iterator.hasNext()) {
            val line = iterator.next()
            val entry = line.split(" ")
            informations[entry[4]] = Door(entry[0], entry[2], entry[1], entry[4])
        }
        bufferedReader.close()
    }
}

fun main(args: Array<String>) {
    println("Running the main function");
    Day24().run("resource/day24.txt", "output/day24.dot");
}

// 169 tolow

// qwf->cnk
// z14->vhm
// z27->mps
// z39->msq
//cnk,mps,msq,qwf,vhm,z14,z27,z39