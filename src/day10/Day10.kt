package day10

import println
import readInput

private const val DAY_ID = "10"

fun main() {
    // up, down, left, right
    val directions = arrayOf(
        intArrayOf(-1, 0),
        intArrayOf(1, 0),
        intArrayOf(0, -1),
        intArrayOf(0, 1),
    )

    data class Cell(val row: Int, val col: Int) {
        fun move(dx: Int, dy: Int): Cell = Cell(row + dx, col + dy)
        fun isInBounds(m: Int, n: Int): Boolean = row in 0 until m && col in 0 until n
    }

    fun solve(input: List<String>, traverse: (grid: List<List<Char>>, start: Cell) -> Int): Int {
        val grid = input.map { it.toList() }
        val m = grid.size
        val n = grid[0].size

        var total = 0
        for (row in 0 until m) {
            for (col in 0 until n) {
                if (grid[row][col] == '0') {
                    total += traverse(grid, Cell(row, col))
                }
            }
        }
        return total
    }

    fun part1(input: List<String>): Int {
        fun traverse(grid: List<List<Char>>, start: Cell): Int {
            val m = grid.size
            val n = grid[0].size

            fun goodMove(curr: Cell, next: Cell): Boolean {
                return next.isInBounds(m, n)
                        && (grid[next.row][next.col].isDigit())
                        && (grid[next.row][next.col] - grid[curr.row][curr.col] == 1)
            }

            var count = 0
            val visited = mutableSetOf<Cell>()
            fun dfs(curr: Cell, length: Int) {
                visited += curr

                // base case
                if (grid[curr.row][curr.col] == '9' && length % 2 == 0) {
                    count++
                    return
                }

                // explore directions
                for ((dx, dy) in directions) {
                    val next = curr.move(dx, dy)
                    if (goodMove(curr, next) && next !in visited) {
                        dfs(next, length + 1)
                    }
                }
            }

            dfs(start, 1)
            return count
        }

        return solve(input, ::traverse)
    }

    fun part2(input: List<String>): Int {
        fun traverse(grid: List<List<Char>>, start: Cell): Int {
            val m = grid.size
            val n = grid[0].size

            fun goodMove(curr: Cell, next: Cell): Boolean {
                return next.isInBounds(m, n)
                        && (grid[next.row][next.col].isDigit())
                        && (grid[next.row][next.col] - grid[curr.row][curr.col] == 1)
            }

            var count = 0
            fun dfs(curr: Cell, visited: Set<Cell>) {
                // base case
                if (grid[curr.row][curr.col] == '9' && visited.size % 2 == 0) {
                    count++
                    return
                }

                // explore directions
                for ((dx, dy) in directions) {
                    val next = curr.move(dx, dy)
                    if (goodMove(curr, next) && next !in visited) {
                        dfs(next, visited + next)
                    }
                }
            }

            dfs(start, setOf(start))
            return count
        }

        return solve(input, ::traverse)
    }

    // test if implementation meets criteria from the description, like:
    check(part1(readInput("day$DAY_ID/Day${DAY_ID}_part1_test1")) == 1)
    check(part1(readInput("day$DAY_ID/Day${DAY_ID}_part1_test2")) == 2)
    check(part1(readInput("day$DAY_ID/Day${DAY_ID}_part1_test3")) == 4)
    check(part1(readInput("day$DAY_ID/Day${DAY_ID}_part1_test4")) == 3)
    check(part1(readInput("day$DAY_ID/Day${DAY_ID}_part1_test5")) == 36)

    check(part2(readInput("day$DAY_ID/Day${DAY_ID}_part2_test1")) == 3)
    check(part2(readInput("day$DAY_ID/Day${DAY_ID}_part2_test2")) == 13)
    check(part2(readInput("day$DAY_ID/Day${DAY_ID}_part2_test3")) == 227)
    check(part2(readInput("day$DAY_ID/Day${DAY_ID}_part2_test4")) == 81)

    val input = readInput("day$DAY_ID/Day$DAY_ID")
    part1(input).println() // answer 582
    part2(input).println() // answer 1302
}
