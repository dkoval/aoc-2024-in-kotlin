package day20

import println
import readInput
import java.util.*
import kotlin.math.abs

private const val DAY_ID = "20"

private enum class Direction(val dr: Int, val dc: Int) {
    UP(-1, 0), DOWN(1, 0), LEFT(0, -1), RIGHT(0, 1)
}

fun main() {
    data class Cell(val row: Int, val col: Int) {
        fun move(d: Direction): Cell = Cell(row + d.dr, col + d.dc)
    }

    fun solve(input: List<String>, maxCheatingTime: Int, minSavingTime: Int): Int {
        val grid = input.map { line -> line.toMutableList() }
        val m = grid.size
        val n = grid[0].size

        fun traverse(start: Cell, end: Cell): Int {
            fun bfs(start: Cell): Map<Cell, Int> {
                val q = ArrayDeque<Cell>().apply { offer(start) }
                // times[(row, col)] - the minimum time to reach (row, col) from the starting cell
                val times = mutableMapOf<Cell, Int>(start to 0)
                while (q.isNotEmpty()) {
                    val curr = q.poll()
                    for (d in Direction.entries) {
                        val next = curr.move(d)
                        if (grid[next.row][next.col] != '#' && times.getOrPut(next) { Int.MAX_VALUE } > times[curr]!! + 1) {
                            q.offer(next)
                            times[next] = times[curr]!! + 1
                        }
                    }
                }
                return times
            }

            val times1 = bfs(start)
            val times2 = bfs(end)

            val baseTime = times1[end]!!
            var count = 0
            for ((c1, t1) in times1) {
                for ((c2, t2) in times2) {
                    // t1 - the minimum time to reach (row1, col1) from the starting cell
                    // t2 - the minimum time to reach (row2, col2) from the ending cell
                    // cheatingTime - the time it takes to get from (row1, col1) to (row2, col2) regardless of the walls
                    val cheatingTime = abs(c1.row - c2.row) + abs(c1.col - c2.col)
                    if (cheatingTime <= maxCheatingTime && baseTime - (t1 + t2 + cheatingTime) >= minSavingTime) {
                        count++
                    }
                }
            }
            return count
        }

        var start: Cell? = null
        var end: Cell? = null
        for (row in 1 until m - 1) {
            for (col in 1 until n - 1) {
                when (grid[row][col]) {
                    'S' -> start = Cell(row, col)
                    'E' -> end = Cell(row, col)
                }
            }
        }

        if (start == null) {
            error("Cold not find the starting cell")
        }

        if (end == null) {
            error("Cold not find the ending cell")
        }

        return traverse(start, end)
    }

    fun part1(input: List<String>, minSavingTime: Int): Int {
        // It takes 2 picoseconds to reach 2 from 1 regardless of the wall
        // Examples 1:
        // 1 # 2
        // Example 2:
        // 1 .
        // # 2
        return solve(input, 2, minSavingTime)
    }

    fun part2(input: List<String>, minSavingTime: Int): Int {
        return solve(input, 20, minSavingTime)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day${DAY_ID}/Day${DAY_ID}_test")
    check(part1(testInput, 2) == 44)
    check(part2(testInput, 50) == 285)

    val input = readInput("day$DAY_ID/Day$DAY_ID")
    part1(input, 100).println() // answer 1375
    part2(input, 100).println() // answer 983054
}
