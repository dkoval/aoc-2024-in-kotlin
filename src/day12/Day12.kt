package day12

import println
import readInput

private const val DAY_ID = "12"

fun main() {
    // up, down, left, right
    val directions = arrayOf(
        intArrayOf(-1, 0),
        intArrayOf(1, 0),
        intArrayOf(0, -1),
        intArrayOf(0, 1)
    )

    data class Region(val area: Int, val perimeter: Int)

    fun part1(input: List<String>): Long {
        val grid = input.map { it.toList() }
        val m = grid.size
        val n = grid[0].size

        val visited = Array(m) { BooleanArray(n) }
        fun traverse(startRow: Int, startCol: Int): Region {
            var area = 0
            var perimeter = 0
            fun dfs(row: Int, col: Int) {
                visited[row][col] = true
                area++
                for ((dx, dy) in directions) {
                    val nextRow = row + dx
                    val nextCol = col + dy
                    if (nextRow !in 0 until m || nextCol !in 0 until n || grid[nextRow][nextCol] != grid[startRow][startCol]) {
                        // count non-shared edges
                        perimeter++
                        continue
                    }

                    if (!visited[nextRow][nextCol]) {
                        dfs(nextRow, nextCol)
                    }
                }
            }

            dfs(startRow, startCol)
            return Region(area, perimeter)
        }

        var total = 0L
        for (row in 0 until m) {
            for (col in 0 until n) {
                if (!visited[row][col]) {
                    val (area, perimeter) = traverse(row, col)
                    total += area * perimeter
                }
            }
        }
        return total
    }

    data class Cell(val row: Int, val col: Int, val dx: Int, val dy: Int)

    fun part2(input: List<String>): Long {
        val grid = input.map { it.toList() }
        val m = grid.size
        val n = grid[0].size

        fun countSides(cells: Set<Cell>): Int {
            var count = 0
            for ((row, col, dx, dy) in cells) {
                if (dx != 0) {
                    count += if (Cell(row, col - 1, dx, dy) !in cells) 1 else 0
                }

                if (dy != 0) {
                    count += if (Cell(row - 1, col, dx, dy) !in cells) 1 else 0
                }
            }
            return count
        }

        val visited = Array(m) { BooleanArray(n) }
        fun traverse(startRow: Int, startCol: Int): Region {
            var area = 0
            val region = mutableSetOf<Cell>()
            fun dfs(row: Int, col: Int) {
                visited[row][col] = true
                area++
                for ((dx, dy) in directions) {
                    val nextRow = row + dx
                    val nextCol = col + dy
                    if (nextRow !in 0 until m || nextCol !in 0 until n || grid[nextRow][nextCol] != grid[startRow][startCol]) {
                        // record cells that contribute to the region's perimeter
                        region += Cell(row, col, dx, dy)
                        continue
                    }

                    if (!visited[nextRow][nextCol]) {
                        dfs(nextRow, nextCol)
                    }
                }
            }

            dfs(startRow, startCol)
            return Region(area, countSides(region))
        }

        var total = 0L
        for (row in 0 until m) {
            for (col in 0 until n) {
                if (!visited[row][col]) {
                    val (area, perimeter) = traverse(row, col)
                    total += area * perimeter
                }
            }
        }
        return total
    }

    // test if implementation meets criteria from the description, like:
    check(part1(readInput("day$DAY_ID/Day${DAY_ID}_part1_test1")) == 140L)
    check(part1(readInput("day$DAY_ID/Day${DAY_ID}_part1_test2")) == 772L)
    check(part1(readInput("day$DAY_ID/Day${DAY_ID}_part1_test3")) == 1930L)

    check(part2(readInput("day$DAY_ID/Day${DAY_ID}_part1_test1")) == 80L)
    check(part2(readInput("day$DAY_ID/Day${DAY_ID}_part1_test2")) == 436L)
    check(part2(readInput("day$DAY_ID/Day${DAY_ID}_part2_test3")) == 236L)
    check(part2(readInput("day$DAY_ID/Day${DAY_ID}_part2_test4")) == 368L)
    check(part2(readInput("day$DAY_ID/Day${DAY_ID}_part1_test3")) == 1206L)

    val input = readInput("day$DAY_ID/Day$DAY_ID")
    part1(input).println() // answer 1546338
    part2(input).println() // answer 978590
}
