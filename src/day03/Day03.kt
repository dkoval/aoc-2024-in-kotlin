package day03

import println
import readInput
import readInputAsString

private const val DAY_ID = "03"

fun main() {
    fun part1(input: String): Int {
        val regex = """mul\((\d{1,3}),(\d{1,3})\)""".toRegex()
        return regex.findAll(input).map { result ->
            val (x, y) = result.destructured
            x.toInt() * y.toInt()
        }.sum()
    }

    fun part2(input: String): Int {
        val regex = """mul\(\d{1,3},\d{1,3}\)|do\(\)|don't\(\)""".toRegex()
        var enabled = true
        return regex.findAll(input).map { it.value }.fold(0) { acc, op ->
            var delta = when (op) {
                "don't()" -> {
                    enabled = false
                    0
                }

                "do()" -> {
                    enabled = true
                    0
                }

                else -> {
                    // op == mul(2,4)
                    if (enabled) {
                        val (x, y) = op.removePrefix("mul(").removeSuffix(")").split(",").map { it.toInt() }
                        x * y
                    } else {
                        0
                    }
                }
            }
            acc + delta
        }
    }

    // test if implementation meets criteria from the description, like:
    check(part1(readInputAsString("day$DAY_ID/Day${DAY_ID}_test1")) == 161)
    check(part2(readInputAsString("day$DAY_ID/Day${DAY_ID}_test2")) == 48)

    val input = readInputAsString("day$DAY_ID/Day$DAY_ID")
    part1(input).println() // answer 166630675
    part2(input).println() // answer 93465710
}
