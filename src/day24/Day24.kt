package day24

import day24.Operator.*
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

private data class Gate(val x: String, val y: String, val op: Operator)

fun main() {
    fun part1(input: String): Long {
        val (rawValues, rawGates) = input.split("\n\n")

        val values = rawValues.split("\n")
            .associateTo(mutableMapOf()) {
                val (wire, value) = it.split(": ")
                wire to value.toInt()
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
        val (rawValues, rawGates) = input.split("\n\n")

        val values = rawValues.split("\n")
            .associate {
                val (wire, value) = it.split(": ")
                wire to value.toInt()
            }

        val gatesRegex = """(\w+) (AND|OR|XOR) (\w+) -> (\w+)""".toRegex()
        val gates = rawGates.split("\n")
            .associateTo(mutableMapOf()) {
                val (x, op, y, out) = gatesRegex.matchEntire(it)!!.destructured
                out to Gate(x, y, Operator.valueOf(op))
            }

        fun prettyPrint(wire: String, depth: Int = 0, maxDepth: Int = 5) {
            if (depth > maxDepth) {
                println("${"  ".repeat(depth)}... (truncated)")
                return
            }

            if (wire in values) {
                println("${"  ".repeat(depth)}$wire = ${values[wire]}")
                return
            }

            val (x, y, op) = gates[wire]!!
            println("${"  ".repeat(depth)}$wire = $op $x $y")
            prettyPrint(x, depth + 1)
            prettyPrint(y, depth + 1)
        }

        fun swapWires(wire1: String, wire2: String) {
            val tmp = gates[wire1]!!
            gates[wire1] = gates[wire2]!!
            gates[wire2] = tmp
        }

        fun failsAt(): Int {
            var i = 0
            while (matchesAt(gates, i)) {
                i++
            }
            return i
        }

        val swaps = mutableListOf<String>()
        repeat(4) {
            val before = failsAt()
            println("Failed at position $before")
            prettyPrint(makeWire('z', before))
            loop@ for (wire1 in gates.keys) {
                for (wire2 in gates.keys) {
                    if (wire1 == wire2) continue
                    swapWires(wire1, wire2)
                    if (failsAt() > before) {
                        swaps += wire1
                        swaps += wire2
                        println("Fixed position $before by swapping $wire1 <-> $wire2\n")
                        break@loop
                    }
                    swapWires(wire1, wire2)
                }
            }
        }
        return swaps.sorted().joinToString(",")
    }

    // test if implementation meets criteria from the description, like:
    check(part1(readInputAsString("day$DAY_ID/Day${DAY_ID}_part1_test1")) == 4L)
    check(part1(readInputAsString("day$DAY_ID/Day${DAY_ID}_part1_test2")) == 2024L)

    val input = readInputAsString("day$DAY_ID/Day$DAY_ID")
    part1(input).println() // answer 36035961805936
    part2(input).println() // answer jqf,mdd,skh,wpd,wts,z11,z19,z37
}

private fun makeWire(c: Char, i: Int): String = "$c${i.toString().padStart(2, '0')}"

private fun matchesAt(gates: Map<String, Gate>, i: Int): Boolean {
    fun matchesWire(wire: String): Boolean {
        if (wire !in gates) return false

        // z[0] = x[0] ^ y[0]
        // z[i] = x[i] ^ y[i] ^ carry[i], if i > 0
        val (x, y, op) = gates[wire]!!
        if (op != XOR) return false

        if (i == 0) return setOf(x, y) == setOf(makeWire('x', 0), makeWire('y', 0))
        return (matchesXor(gates, x, i) && matchesCarry(gates, y, i))
                || (matchesXor(gates, y, i) && matchesCarry(gates, x, i))
    }

    return matchesWire(makeWire('z', i))
}

private fun matchesXor(gates: Map<String, Gate>, wire: String, i: Int): Boolean {
    if (wire !in gates) return false

    // z[i] = x[i] ^ y[i]
    val (x, y, op) = gates[wire]!!
    if (op != XOR) return false
    return setOf(x, y) == setOf(makeWire('x', i), makeWire('y', i))
}

private fun matchesCarry(gates: Map<String, Gate>, wire: String, i: Int): Boolean {
    if (wire !in gates) return false

    // carry[1] = x[0] & y[0]
    // carry[i] = (x[i - 1] & y[i - 1]) || ((x[i - 1] ^ y[i - 1]) & carry[i - 1]), if i > 1
    //            |-------------------|    |------------------------------------|
    //            direct carry             complex carry
    val (x, y, op) = gates[wire]!!
    if (i == 1) {
        if (op != AND) return false
        return setOf(x, y) == setOf(makeWire('x', 0), makeWire('y', 0))
    }

    if (op != OR) return false
    return (matchesDirectCarry(gates, x, i - 1) && matchesComplexCarry(gates, y, i - 1))
            || (matchesDirectCarry(gates, y, i - 1) && matchesComplexCarry(gates, x, i - 1))
}

private fun matchesDirectCarry(gates: Map<String, Gate>, wire: String, i: Int): Boolean {
    if (wire !in gates) return false

    val (x, y, op) = gates[wire]!!
    if (op != AND) return false
    return setOf(x, y) == setOf(makeWire('x', i), makeWire('y', i))
}

private fun matchesComplexCarry(gates: Map<String, Gate>, wire: String, i: Int): Boolean {
    if (wire !in gates) return false

    val (x, y, op) = gates[wire]!!
    if (op != AND) return false
    return (matchesXor(gates, x, i) && matchesCarry(gates, y, i))
            || (matchesXor(gates, y, i) && matchesCarry(gates, x, i))
}
