import java.io.BufferedReader
import java.io.File
import java.util.*
import kotlin.math.abs
import kotlin.math.pow

data class Computer(var varA: Long, var varB: Long, var varC: Long, val instruction: List<Int>) {

    fun solve(): MutableList<Long> {
        var result = mutableListOf<Long>()
        var index = 0
        var nbLoop = 0
        while (index<instruction.size) {
            nbLoop++
            val currentInstruction = instruction[index++]
            val operand = instruction[index++]
            println("############@")
            when (currentInstruction) {
                0 -> {
                    println("instruction: $currentInstruction operand: $operand combo: ${getCombo(operand)}")
                    println("A: $varA B: $varB C: $varC")
                    varA = (varA/2.0.pow(getCombo(operand).toInt())).toLong()
                }
                1 -> {
                    println("instruction: $currentInstruction operand: $operand ")
                    println("A: $varA B: $varB C: $varC")
                    varB = (varB xor operand.toLong())
                }
                2 -> {
                    println("instruction: $currentInstruction operand: $operand combo: ${getCombo(operand)}")
                    println("A: $varA B: $varB C: $varC")
                    varB = Math.floorMod(getCombo(operand),8).toLong() and 0b111
                }
                3 -> {
                    println("instruction: $currentInstruction operand: $operand ")
                    println("A: $varA B: $varB C: $varC")
                    if (varA != 0L) {
                        index = operand
                    }
                }
                4 -> {
                    println("instruction: $currentInstruction operand: $operand ")
                    println("A: $varA B: $varB C: $varC")
                    varB = (varB xor varC)
                }
                5 -> {
                    println("instruction: $currentInstruction operand: $operand combo: ${getCombo(operand)}")
                    println("A: $varA B: $varB C: $varC")
                    result.add(Math.floorMod(getCombo(operand),8).toLong())
                }
                6 -> {
                    println("instruction: $currentInstruction operand: $operand  combo: ${getCombo(operand)}")
                    println("A: $varA B: $varB C: $varC")
                    varB = (varA/2.0.pow(getCombo(operand).toInt())).toLong()
                }
                7 -> {
                    println("instruction: $currentInstruction operand: $operand  combo: ${getCombo(operand)}")
                    println("A: $varA B: $varB C: $varC")
                    varC = (varA/2.0.pow(getCombo(operand).toInt())).toLong()
                }
            }
            println("A: $varA B: $varB C: $varC")
        }
        println("nbLoop $nbLoop")
        return result
    }

    private fun getCombo(value: Int): Long {
        return when(value) {
            0 -> 0L
            1 -> 1L
            2 -> 2L
            3 -> 3L
            4 -> varA
            5 -> varB
            6 -> varC
            else -> throw Error("combo operand 7")
        }
        /*
            Combo operands 0 through 3 represent literal values 0 through 3.
            Combo operand 4 represents the value of register A.
            Combo operand 5 represents the value of register B.
            Combo operand 6 represents the value of register C.
            Combo operand 7 is reserved and will not appear in valid programs.
         */
    }
}

/*
The eight instructions are as follows:

The adv instruction (opcode 0) performs division. The numerator is the value in the A register. The denominator is found by raising 2 to the power of the instruction's combo operand. (So, an operand of 2 would divide A by 4 (2^2); an operand of 5 would divide A by 2^B.) The result of the division operation is truncated to an integer and then written to the A register.

The bxl instruction (opcode 1) calculates the bitwise XOR of register B and the instruction's literal operand, then stores the result in register B.

The bst instruction (opcode 2) calculates the value of its combo operand modulo 8 (thereby keeping only its lowest 3 bits), then writes that value to the B register.

The jnz instruction (opcode 3) does nothing if the A register is 0. However, if the A register is not zero, it jumps by setting the instruction pointer to the value of its literal operand; if this instruction jumps, the instruction pointer is not increased by 2 after this instruction.

The bxc instruction (opcode 4) calculates the bitwise XOR of register B and register C, then stores the result in register B. (For legacy reasons, this instruction reads an operand but ignores it.)

The out instruction (opcode 5) calculates the value of its combo operand modulo 8, then outputs that value. (If a program outputs multiple values, they are separated by commas.)

The bdv instruction (opcode 6) works exactly like the adv instruction except that the result is stored in the B register. (The numerator is still read from the A register.)

The cdv instruction (opcode 7) works exactly like the adv instruction except that the result is stored in the C register. (The numerator is still read from the A register.)
 */

class Day17 {

    fun run(fileName: String) {
        val computer = createData(fileName)
        println("final result ${computer.solve().joinToString(",") }")
        println("A: ${computer.varA} B: ${computer.varB} C: ${computer.varC}")
    }

    fun run2(fileName: String) {
        val computer = createData(fileName)
        var res = computer.instruction
            .reversed()
            .map { it.toLong() }
            .fold(listOf(0L)) { candidates, instruction ->
                candidates.flatMap { candidate ->
                    val shifted = candidate shl 3
                    (shifted..shifted + 8).mapNotNull { attempt ->
                        computer.copy().run {
                            varA = attempt
                            attempt.takeIf { solve().first() == instruction }
                        }
                    }
                }
            }.first()

        println("res $res")
    }

    private fun createData(fileName: String): Computer {
        val bufferedReader: BufferedReader = File(fileName).bufferedReader()
        val iterator = bufferedReader.lineSequence().iterator()
        val reg = Regex("Register [ABC]: ([0-9]+)")
        val varA = reg.find(iterator.next())!!.groups[1]!!.value.toLong() ?: 0
        val varB = reg.find(iterator.next())!!.groups[1]!!.value.toLong() ?: 0
        val varC = reg.find(iterator.next())!!.groups[1]!!.value.toLong() ?: 0
        iterator.next()
        val instruction = iterator.next().split(" ")[1].split(',').map { it.toInt() }
        bufferedReader.close()
        return Computer(varA, varB, varC, instruction)
    }
}

fun main(args: Array<String>) {
    println("Running the main function");
    Day17().run("resource/day17.txt")
    Day17().run2("resource/day17.txt")
}

// 5,6,0,3,0,2,4,3,6
// 5,6,0,3,0,2,4,3,6
// 2,7,2,5,1,2,7,3,7