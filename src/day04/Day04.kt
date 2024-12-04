package day04

import println
import readInput

private const val DAY_ID = "04"

fun main() {
    data class Cell(val row: Int, val col: Int)

    val dirs = listOf(-1 to 0, 1 to 0, 0 to -1, 0 to 1, -1 to -1, -1 to 1, 1 to -1, 1 to 1)

    fun part1(input: List<String>): Int {
        val keyword = "XMAS"

        val grid = input.map { it.toList() }
        val m = grid.size
        val n = grid[0].size

        // records positions of X, M, A, S letters
        val lookup = mutableMapOf<Char, MutableSet<Cell>>()
        for (row in 0 until m) {
            for (col in 0 until n) {
                val c = grid[row][col]
                if (c in keyword) {
                    lookup.getOrPut(c) { mutableSetOf() } += Cell(row, col)
                }
            }
        }

        var total = 0
        for ((row, col) in lookup['X']!!) {
            for ((dx, dy) in dirs) {
                var found = true
                for (i in 1 until keyword.length) {
                    if (Cell(row + i * dx, col + i * dy) !in lookup[keyword[i]]!!) {
                        found = false
                        break
                    }
                }
                total += if (found) 1 else 0
            }
        }
        return total
    }

    fun part2(input: List<String>): Int {
        val keyword = "MAS"

        val grid = input.map { it.toList() }
        val m = grid.size
        val n = grid[0].size

        // records positions of X, M, A, S letters
        val lookup = mutableMapOf<Char, MutableSet<Cell>>()
        for (row in 0 until m) {
            for (col in 0 until n) {
                val c = grid[row][col]
                if (c in keyword) {
                    lookup.getOrPut(c) { mutableSetOf() } += Cell(row, col)
                }
            }
        }

        var total = 0
        for ((row, col) in lookup['A']!!) {
            when {
                Cell(row - 1, col - 1) in lookup['M']!!
                        && Cell(row + 1, col - 1) in lookup['M']!!
                        && Cell(row - 1, col + 1) in lookup['S']!!
                        && Cell(row + 1, col + 1) in lookup['S']!! -> {
                    total += 1
                }

                Cell(row - 1, col - 1) in lookup['S']!!
                        && Cell(row + 1, col - 1) in lookup['S']!!
                        && Cell(row - 1, col + 1) in lookup['M']!!
                        && Cell(row + 1, col + 1) in lookup['M']!! -> {
                    total += 1
                }

                Cell(row - 1, col - 1) in lookup['M']!!
                        && Cell(row + 1, col - 1) in lookup['S']!!
                        && Cell(row - 1, col + 1) in lookup['M']!!
                        && Cell(row + 1, col + 1) in lookup['S']!! -> {
                    total += 1
                }

                Cell(row - 1, col - 1) in lookup['S']!!
                        && Cell(row + 1, col - 1) in lookup['M']!!
                        && Cell(row - 1, col + 1) in lookup['S']!!
                        && Cell(row + 1, col + 1) in lookup['M']!! -> {
                    total += 1
                }
            }
        }
        return total
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day$DAY_ID/Day${DAY_ID}_test")
    check(part1(testInput) == 18)
    check(part2(testInput) == 9)

    val input = readInput("day$DAY_ID/Day$DAY_ID")
    part1(input).println() // answer 2532
    part2(input).println() // answer 1941
}
