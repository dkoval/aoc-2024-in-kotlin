package day24

import println
import readInputAsString

private const val DAY_ID = "24"

private enum class Operator {
    AND {
        override fun invoke(a: Int, b: Int): Int = a and b
    },

    OR {
        override fun invoke(a: Int, b: Int): Int = a or b
    },

    XOR {
        override fun invoke(a: Int, b: Int): Int = a xor b
    };

    abstract operator fun invoke(a: Int, b: Int): Int
}

fun main() {
    data class Gate(val x: String, val y: String, val op: Operator)

    fun part1(input: String): Long {
        val (rawValues, rawGates) = input.split("\n\n")

        val values = rawValues.split("\n")
            .associateTo(mutableMapOf()) {
                val (name, value) = it.split(": ")
                name to value.toInt()
            }

        val gatesRegex = """(\w+) (AND|OR|XOR) (\w+) -> (\w+)""".toRegex()
        val gates = rawGates.split("\n")
            .associate {
                val (x, op, y, out) = gatesRegex.matchEntire(it)!!.destructured
                out to Gate(x, y, Operator.valueOf(op))
            }

        fun calc(wire: String): Int {
            if (wire in values) {
                return values[wire]!!
            }

            val (x, y, op) = gates[wire]!!
            return op(calc(x), calc(y)).also { values[wire] = it }
        }

        return gates.keys.filter { it.startsWith("z") }
            .sorted()
            .fold(mutableListOf<Int>()) { ans, wire ->
                ans += calc(wire)
                ans
            }
            .asReversed()
            .fold(0L) { acc, bit -> acc * 2 + bit }
    }

    fun part2(input: String): String {
        TODO()
    }

    // test if implementation meets criteria from the description, like:
    check(part1(readInputAsString("day$DAY_ID/Day${DAY_ID}_part1_test1")) == 4L)
    check(part1(readInputAsString("day$DAY_ID/Day${DAY_ID}_part1_test2")) == 2024L)

    //check(part2(readInputAsString("day$DAY_ID/Day${DAY_ID}_part2_test1")) == "z00,z01,z02,z05")

    val input = readInputAsString("day$DAY_ID/Day$DAY_ID")
    part1(input).println() // answer 36035961805936
    //part2(input).println()
}
