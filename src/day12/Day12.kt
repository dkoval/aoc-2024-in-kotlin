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

    data class RegionInfo(val area: Int, val other: Int)

    fun solve(
        input: List<String>,
        traverse: (grid: List<List<Char>>, startRow: Int, startCol: Int, visited: Array<BooleanArray>) -> RegionInfo
    ): Int {
        val grid = input.map { it.toList() }
        val m = grid.size
        val n = grid[0].size

        var total = 0
        val visited = Array(m) { BooleanArray(n) }
        for (row in 0 until m) {
            for (col in 0 until n) {
                if (!visited[row][col]) {
                    val (area, other) = traverse(grid, row, col, visited)
                    total += area * other
                }
            }
        }
        return total
    }

    fun part1(input: List<String>): Int {
        fun traverse(grid: List<List<Char>>, startRow: Int, startCol: Int, visited: Array<BooleanArray>): RegionInfo {
            val m = grid.size
            val n = grid[0].size

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
            return RegionInfo(area, perimeter)
        }

        return solve(input, ::traverse)
    }

    data class Cell(val row: Int, val col: Int, val dx: Int, val dy: Int)

    fun part2(input: List<String>): Int {
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

        fun traverse(grid: List<List<Char>>, startRow: Int, startCol: Int, visited: Array<BooleanArray>): RegionInfo {
            val m = grid.size
            val n = grid[0].size

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
            return RegionInfo(area, countSides(region))
        }

        return solve(input, ::traverse)
    }

    // test if implementation meets criteria from the description, like:
    check(part1(readInput("day$DAY_ID/Day${DAY_ID}_part1_test1")) == 140)
    check(part1(readInput("day$DAY_ID/Day${DAY_ID}_part1_test2")) == 772)
    check(part1(readInput("day$DAY_ID/Day${DAY_ID}_part1_test3")) == 1930)

    check(part2(readInput("day$DAY_ID/Day${DAY_ID}_part1_test1")) == 80)
    check(part2(readInput("day$DAY_ID/Day${DAY_ID}_part1_test2")) == 436)
    check(part2(readInput("day$DAY_ID/Day${DAY_ID}_part2_test3")) == 236)
    check(part2(readInput("day$DAY_ID/Day${DAY_ID}_part2_test4")) == 368)
    check(part2(readInput("day$DAY_ID/Day${DAY_ID}_part1_test3")) == 1206)

    val input = readInput("day$DAY_ID/Day$DAY_ID")
    part1(input).println() // answer 1546338
    part2(input).println() // answer 978590
}
