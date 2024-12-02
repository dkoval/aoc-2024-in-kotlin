package day02

import println
import readInput
import kotlin.math.abs

private const val DAY_ID = "02"

fun main() {
    fun isSafePart1(levels: List<Int>): Boolean {
        val increasing = levels[1] > levels[0]
        for (i in 1 until levels.size) {
            if (levels[i] > levels[i - 1] != increasing) {
                return false
            }

            if (abs(levels[i] - levels[i - 1]) !in 1..3) {
                return false
            }
        }
        return true
    }

    fun isSafePart2(levels: List<Int>): Boolean {
        if (isSafePart1(levels)) {
            return true
        }

        // Brute force: exclude levels[i] and see if the levels remain safe
        return (0 until levels.size).any { i ->
            val newLevels = levels.subList(0, i).toMutableList() + levels.subList(i + 1, levels.size)
            isSafePart1(newLevels)
        }
    }

    fun solve(input: List<String>, isSafe: (levels: List<Int>) -> Boolean): Int {
        val reports = input.map { line -> line.split(" ").map { it.toInt() } }
        return reports.count { levels -> isSafe(levels) }
    }

    fun part1(input: List<String>): Int = solve(input) { levels -> isSafePart1(levels) }

    fun part2(input: List<String>): Int = solve(input) { levels -> isSafePart2(levels) }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day$DAY_ID/Day${DAY_ID}_test")
    check(part1(testInput) == 2)
    check(part2(testInput) == 4)

    val input = readInput("day$DAY_ID/Day$DAY_ID")
    part1(input).println() // answer 202
    part2(input).println() // answer 271
}
