package day05

import println
import readInputAsString

private const val DAY_ID = "05"

fun main() {
    data class Input(
        val before: Map<Int, Set<Int>>,
        val updates: List<List<Int>>
    )

    fun processInput(input: String): Input {
        val (rawRules, rawUpdates) = input.split("\n\n")

        // before[x] - the list of numbers coming before x
        val before = rawRules.split("\n")
            .fold(mutableMapOf<Int, MutableSet<Int>>()) { acc, line ->
                val (x, y) = line.split("|").map { it.toInt() }
                acc.getOrPut(y) { mutableSetOf() } += x
                acc
            }

        val updates = rawUpdates.split("\n")
            .map { it.split(",").map { it.toInt() } }

        return Input(before, updates)
    }

    fun isGoodUpdate(nums: List<Int>, before: Map<Int, Set<Int>>): Boolean {
        for (i in 0 until nums.size - 1) {
            // check numbers before x
            for (j in i + 1 until nums.size) {
                if (nums[j] in (before[nums[i]] ?: emptySet<Int>())) {
                    return false
                }
            }
        }
        return true
    }

    fun part1(input: String): Int {
        val (before, updates) = processInput(input)

        return updates.asSequence()
            .filter { isGoodUpdate(it, before) }
            .map { xs -> xs[xs.size / 2] }
            .sum()
    }

    fun part2(input: String): Int {
        val (before, updates) = processInput(input)

        fun fixUpdate(update: List<Int>): List<Int> = update.sortedWith { x, y ->
            when {
                x in (before[y] ?: emptySet<Int>()) -> -1
                y in (before[x] ?: emptySet<Int>()) -> 1
                else -> 0
            }
        }

        return updates.asSequence()
            .filterNot { nums -> isGoodUpdate(nums, before) }
            .map { nums -> fixUpdate(nums) }
            .map { xs -> xs[xs.size / 2] }
            .sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInputAsString("day$DAY_ID/Day${DAY_ID}_test")
    check(part1(testInput) == 143)
    check(part2(testInput) == 123)

    val input = readInputAsString("day$DAY_ID/Day$DAY_ID")
    part1(input).println() // answer 5091
    part2(input).println() // answer 4681
}
