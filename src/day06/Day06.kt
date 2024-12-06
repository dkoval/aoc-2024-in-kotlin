package day06

import println
import readInput

private const val DAY_ID = "06"

private enum class Direction(val dx: Int, val dy: Int) {
    UP(-1, 0) {
        override fun turnRight(): Direction = RIGHT
    },
    DOWN(1, 0) {
        override fun turnRight(): Direction = LEFT
    },
    LEFT(0, -1) {
        override fun turnRight(): Direction = UP
    },
    RIGHT(0, 1) {
        override fun turnRight(): Direction = DOWN
    };

    abstract fun turnRight(): Direction
}

fun main() {
    data class Cell(val row: Int, val col: Int) {
        fun move(d: Direction): Cell = Cell(row + d.dx, col + d.dy)
        fun isInBounds(m: Int, n: Int): Boolean = row in 0 until m && col in 0 until n
    }

    fun part1(input: List<String>): Int {
        val grid = input.map { it.toMutableList() }
        val m = grid.size
        val n = grid[0].size

        fun countVisitedCells(start: Cell): Int {
            var curr = start
            var d = Direction.UP
            val visited = mutableSetOf<Cell>()
            while (true) {
                visited += curr
                val next = curr.move(d)

                if (!next.isInBounds(m, n)) {
                    break
                }

                if (grid[next.row][next.col] == '#') {
                    d = d.turnRight()
                } else {
                    curr = next
                }
            }
            return visited.size
        }

        for (row in 0 until m) {
            for (col in 0 until n) {
                if (grid[row][col] == '^') {
                    return countVisitedCells(Cell(row, col))
                }
            }
        }
        return -1
    }

    fun part2(input: List<String>): Int {
        val grid = input.map { it.toMutableList() }
        val m = grid.size
        val n = grid[0].size

        fun findStart(): Cell {
            for (row in 0 until m) {
                for (col in 0 until n) {
                    if (grid[row][col] == '^') {
                        return Cell(row, col)
                    }
                }
            }
            error("Couldn't find the starting cell")
        }

        fun loopExists(start: Cell): Boolean {
            var curr = start
            var d = Direction.UP
            val visited = mutableSetOf<Pair<Cell, Direction>>()
            while (true) {
                if (curr to d in visited) {
                    return true
                }

                visited += curr to d
                val next = curr.move(d)

                if (!next.isInBounds(m, n)) {
                    return false
                }

                if (grid[next.row][next.col] == '#') {
                    d = d.turnRight()
                } else {
                    curr = next
                }
            }
        }

        val start = findStart()
        var numLoops = 0
        for (row in 0 until m) {
            for (col in 0 until n) {
                if (grid[row][col] != '.') {
                    continue
                }

                grid[row][col] = '#'
                numLoops += if (loopExists(start)) 1 else 0
                grid[row][col] = '.'
            }
        }
        return numLoops
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day$DAY_ID/Day${DAY_ID}_test")
    check(part1(testInput) == 41)
    check(part2(testInput) == 6)

    val input = readInput("day$DAY_ID/Day$DAY_ID")
    part1(input).println() // answer 5551
    part2(input).println() // answer 1939
}
