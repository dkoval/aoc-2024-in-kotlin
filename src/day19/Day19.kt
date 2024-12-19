package day19

import println
import readInputAsString

private const val DAY_ID = "19"

fun main() {
    fun part1(input: String): Int {
        val (rawPatterns, rawDesigns) = input.split("\n\n")

        val towels = rawPatterns.split(", ").toSet()
        val designs = rawDesigns.split("\n")

        fun possible(design: String): Boolean {
            val cache = mutableMapOf<String, Boolean>()
            fun canSplit(start: Int): Boolean {
                if (start == design.length) {
                    return true
                }

                for (i in start until design.length) {
                    val prefix = design.substring(start, i + 1)
                    if (prefix in towels && cache.getOrPut(prefix) { canSplit(i + 1) }) {
                        return true
                    }
                }
                return false
            }

            return canSplit(0)
        }

        return designs.count { possible(it) }
    }

    fun part2(input: String): Long {
        val (rawPatterns, rawDesigns) = input.split("\n\n")

        val towels = rawPatterns.split(", ").toSet()
        val designs = rawDesigns.split("\n")

        fun numWaysToDesign(design: String): Long {
            val cache = mutableMapOf<Int, Long>()
            fun calcNumWays(start: Int): Long {
                if (start == design.length) {
                    return 1L
                }

                // already solved?
                if (start in cache) {
                    return cache[start]!!
                }

                var count = 0L
                for (i in start until design.length) {
                    val prefix = design.substring(start, i + 1)
                    if (prefix in towels) {
                        count += calcNumWays(i + 1)
                    }
                }
                return count.also { cache[start] = it }
            }

            return calcNumWays(0)
        }

        return designs.sumOf { numWaysToDesign(it) }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInputAsString("day$DAY_ID/Day${DAY_ID}_test")
    check(part1(testInput) == 6)
    check(part2(testInput) == 16L)

    val input = readInputAsString("day$DAY_ID/Day$DAY_ID")
    part1(input).println() // answer 369
    part2(input).println() // answer 761826581538190
}
