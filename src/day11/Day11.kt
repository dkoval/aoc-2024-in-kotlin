package day11

import println
import readInputAsString

private const val DAY_ID = "11"

fun main() {
    fun part1(input: String): Int {
        val initial = input.split(" ").map { it.toLong() }
        val numBlinks = 25

        var stones = initial
        repeat(numBlinks) { i ->
            val newStones = mutableListOf<Long>()
            for (x in stones) {
                when {
                    x == 0L -> newStones += 1L
                    x.toString().length % 2 == 0 -> {
                        val s = x.toString()
                        val mid = s.length / 2
                        val lx = s.substring(0, mid).toLong()
                        val rx = s.substring(mid).toLong()
                        newStones += lx
                        newStones += rx
                    }

                    else -> newStones += x * 2024
                }
            }
            stones = newStones
        }
        return stones.size
    }

    fun part2(input: String): Long {
        val initial = input.split(" ").map { it.toLong() }
        val numBlinks = 75

        var counts = initial.associate { x -> x to 1L }
        repeat(numBlinks) {
            val newCounts = mutableMapOf<Long, Long>()
            for ((x, count) in counts) {
                when {
                    x == 0L -> newCounts[1] = (newCounts[1] ?: 0) + count
                    x.toString().length % 2 == 0 -> {
                        val s = x.toString()
                        val mid = s.length / 2
                        val lx = s.substring(0, mid).toLong()
                        val rx = s.substring(mid).toLong()
                        newCounts[lx] = (newCounts[lx] ?: 0) + count
                        newCounts[rx] = (newCounts[rx] ?: 0) + count
                    }

                    else -> {
                        val y = x * 2024
                        newCounts[y] = (newCounts[y] ?: 0) + count
                    }
                }
            }
            counts = newCounts
        }
        return counts.values.sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInputAsString("day$DAY_ID/Day${DAY_ID}_test")
    check(part1(testInput) == 55312)

    val input = readInputAsString("day$DAY_ID/Day$DAY_ID")
    part1(input).println() // answer 189092
    part2(input).println()
}
