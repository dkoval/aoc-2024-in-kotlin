package day17

import println
import readInputAsString

private const val DAY_ID = "17"

fun main() {
    fun part1(input: String): String {
        val (registers, rawProgram) = input.split("\n\n")

        var (a, b, c) = registers.split("\n").map { it.drop("Register ?: ".length).toInt() }
        val program = rawProgram.drop("Program: ".length).split(",").map { it.toInt() }

        fun combo(operand: Int): Int = when (operand) {
            in 0..3 -> operand
            4 -> a
            5 -> b
            6 -> c
            else -> error("Unsupported operand: $operand")
        }

        val out = mutableListOf<Int>()

        var index = 0
        while (index + 1 < program.size) {
            val opcode = program[index]
            val operand = program[index + 1]
            when (opcode) {
                0 -> a = a shr combo(operand)
                1 -> b = b xor operand
                2 -> b = combo(operand) % 8
                3 -> if (a != 0) {
                    index = operand
                    continue
                }

                4 -> b = b xor c
                5 -> out += combo(operand) % 8
                6 -> b = a shr combo(operand)
                7 -> c = a shr combo(operand)
                else -> error("Unsupported opcode: $opcode")
            }
            index += 2
        }
        return out.joinToString(separator = ",")
    }

    fun part2(input: String): Long {
        val (_, rawProgram) = input.split("\n\n")

        // Input analysis:
        // - only register `a` is used in the program.
        // - registers `b` and `c` are used as temporary variables.
        // - meaningful operations are a = a >> 3 + loop.
        val program = rawProgram.drop("Program: ".length).split(",").map { it.toInt() }

        fun runProgram(_a: Long, _b: Long = 0L, _c: Long = 0L): List<Int> {
            var a = _a
            var b = _b
            var c = _c

            fun combo(operand: Int): Long = when (operand) {
                in 0..3 -> operand.toLong()
                4 -> a
                5 -> b
                6 -> c
                else -> error("Unsupported operand: $operand")
            }

            val out = mutableListOf<Int>()
            var index = 0
            while (index + 1 < program.size) {
                val opcode = program[index]
                val operand = program[index + 1]
                when (opcode) {
                    0 -> a = a shr combo(operand).toInt()
                    1 -> b = b xor operand.toLong()
                    2 -> b = combo(operand) % 8
                    3 -> if (a != 0L) {
                        index = operand
                        continue
                    }

                    4 -> b = b xor c
                    5 -> out += (combo(operand) % 8).toInt()
                    6 -> b = a shr combo(operand).toInt()
                    7 -> c = a shr combo(operand).toInt()
                    else -> error("Unsupported opcode: $opcode")
                }
                index += 2
            }
            return out
        }

        // Recap: meaningful operations are a = a >> 3 + loop.
        // Iterate in reverse order to reconstruct the value of `a` at the beginning of the program, i.e.
        // i = 0    : a = a >> 3 yields program[0]
        // i = 1    : a = a >> 3 yields program[1]
        // ...
        // i = n - 1: a = a >> 3 yields program[n - 1] <- 3 most significant bits of `a` form program[n - 1]
        // Also, note that ((a << 3) + delta) >> 3 = a for any `delta` in [0, 7].
        //                   ^----^ makes space for the next 3-bits value of `delta`
        var candidates = mutableListOf(0L)
        for (i in 0 until program.size) {
            val newCandidates = mutableListOf<Long>()
            for (candidate in candidates) {
                for (delta in 0 until (1 shl 3)) {
                    val newCandidate = (candidate shl 3) + delta
                    if (runProgram(newCandidate) == program.takeLast(i + 1)) {
                        newCandidates += newCandidate
                    }
                }
            }
            candidates = newCandidates
        }
        return candidates.first()
    }

    // test if implementation meets criteria from the description, like:
    check(part1(readInputAsString("day$DAY_ID/Day${DAY_ID}_part1_test")) == "4,6,3,5,6,3,5,2,1,0")
    check(part2(readInputAsString("day$DAY_ID/Day${DAY_ID}_part2_test")) == 117440L)

    val input = readInputAsString("day$DAY_ID/Day$DAY_ID")
    part1(input).println() // answer 4,1,5,3,1,5,3,5,7
    part2(input).println() // answer 164542125272765
}
