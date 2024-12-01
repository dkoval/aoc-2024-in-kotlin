package day01

import println
import readInput
import kotlin.collections.mutableMapOf
import kotlin.math.abs

private const val DAY_ID = "01"

fun main() {
    fun part1(input: List<String>): Long {
        val xs = mutableListOf<Long>()
        val ys = mutableListOf<Long>()

        for (line in input) {
            val (x, y) = line.split("""\s+""".toRegex()).asSequence()
                .map { it.trim() }
                .map { it.toLong() }
                .toList()
            xs += x
            ys += y
        }

        xs.sort()
        ys.sort()

        return xs.zip(ys).fold(0L) { acc, (x, y) ->
            acc + abs(x - y)
        }
    }

    fun part2(input: List<String>): Long {
        val xs = mutableListOf<Long>()
        val counts = mutableMapOf<Long, Int>()

        for (line in input) {
            val (x, y) = line.split("""\s+""".toRegex()).asSequence()
                .map { it.trim() }
                .map { it.toLong() }
                .toList()

            xs += x
            counts[y] = (counts[y] ?: 0) + 1
        }

        return xs.fold(0L) { acc, x ->
            acc + x * (counts[x] ?: 0)
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day$DAY_ID/Day${DAY_ID}_test")
    check(part1(testInput) == 11L)
    check(part2(testInput) == 31L)

    val input = readInput("day$DAY_ID/Day$DAY_ID")
    part1(input).println() // answer 1151792
    part2(input).println() // answer 21790168
}
