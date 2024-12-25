package day25

import println
import readInputAsString

private const val DAY_ID = "25"

fun main() {
    fun part1(input: String): Int {
        val numPins = 5

        val grids = input.split("\n\n")
            .map { block -> block.split("\n").map { it.toList() } }


        fun isLock(grid: List<List<Char>>): Boolean = grid[0].all { it == '#' } && grid[numPins + 1].all { it == '.' }
        fun isKey(grid: List<List<Char>>): Boolean = grid[0].all { it == '.' } && grid[numPins + 1].all { it == '#' }

        fun fit(key: List<List<Char>>, lock: List<List<Char>>): Boolean {
            fun heights(grid: List<List<Char>>): List<Int> {
                val ans = mutableListOf<Int>()
                for (col in 0 until numPins) {
                    var height = 0
                    for (row in 1..numPins) {
                        if (grid[row][col] == '#') {
                            height++
                        }
                    }
                    ans += height
                }
                return ans
            }

            val xs = heights(key)
            val ys = heights(lock)
            for ((x, y) in xs.zip(ys)) {
                if (x + y > numPins) {
                    return false
                }
            }
            return true
        }

        val locks = grids.filter { isLock(it) }
        val keys = grids.filter { isKey(it) }

        var count = 0
        for (key in keys) {
            for (lock in locks) {
                if (fit(key, lock)) {
                    count++
                }
            }
        }
        return count
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInputAsString("day$DAY_ID/Day${DAY_ID}_test")
    check(part1(testInput) == 3)

    val input = readInputAsString("day$DAY_ID/Day$DAY_ID")
    part1(input).println() // answer 3439
}
