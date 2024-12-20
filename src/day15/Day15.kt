package day15

import println
import readInputAsString

private const val DAY_ID = "15"

private enum class Direction(val dr: Int, val dc: Int) {
    UP(-1, 0) {
        override fun opposite(): Direction = DOWN
    },

    DOWN(1, 0) {
        override fun opposite(): Direction = UP
    },

    LEFT(0, -1) {
        override fun opposite(): Direction = RIGHT
    },

    RIGHT(0, 1) {
        override fun opposite(): Direction = LEFT
    };

    abstract fun opposite(): Direction

    companion object {
        fun from(c: Char): Direction = when (c) {
            '^' -> UP
            'v' -> DOWN
            '<' -> LEFT
            '>' -> RIGHT
            else -> error("Unsupported direction: $c")
        }
    }
}

fun main() {
    data class Cell(val row: Int, val col: Int) {
        fun move(d: Direction): Cell = Cell(row + d.dr, col + d.dc)
    }

    fun findRobot(grid: List<List<Char>>): Cell {
        val m = grid.size
        val n = grid[0].size

        for (row in 1 until m - 1) {
            for (col in 1 until n - 1) {
                if (grid[row][col] == '@') {
                    return (Cell(row, col))
                }
            }
        }
        error("Could not find the robot")
    }

    fun sumBoxes(grid: List<List<Char>>, box: Char): Int {
        val m = grid.size
        val n = grid[0].size

        var total = 0
        for (row in 1 until m - 1) {
            for (col in 1 until n - 1) {
                if (grid[row][col] == box) {
                    total += 100 * row + col
                }
            }
        }
        return total
    }

    fun part1(input: String): Int {
        val (rawGrid, rawMoves) = input.split("\n\n")

        val grid = rawGrid.split("\n")
            .map { line -> line.toMutableList() }

        val moves = rawMoves.split("\n").asSequence()
            .map { line -> line.map { c -> Direction.from(c) } }
            .flatten()
            .toList()

        fun moveRobot(start: Cell) {
            var curr = start
            for (d in moves) {
                val next = curr.move(d)
                when (grid[next.row][next.col]) {
                    '.' -> {
                        grid[next.row][next.col] = '@'
                        grid[curr.row][curr.col] = '.'
                        curr = next
                    }

                    'O' -> {
                        var nextEmpty = next.move(d)
                        while (grid[nextEmpty.row][nextEmpty.col] == 'O') {
                            nextEmpty = nextEmpty.move(d)
                        }

                        // hit the wall?
                        if (grid[nextEmpty.row][nextEmpty.col] == '#') {
                            continue
                        }

                        // shift boxes
                        while (nextEmpty != next) {
                            grid[nextEmpty.row][nextEmpty.col] = 'O'
                            nextEmpty = nextEmpty.move(d.opposite())
                        }

                        grid[next.row][next.col] = '@'
                        grid[curr.row][curr.col] = '.'
                        curr = next
                    }
                }
            }
        }

        val robot = findRobot(grid)
        moveRobot(robot)
        return sumBoxes(grid, 'O')
    }

    fun part2(input: String): Int {
        val (rawGrid, rawMoves) = input.split("\n\n")

        val gridMapping = mapOf(
            '#' to listOf('#', '#'),
            'O' to listOf('[', ']'),
            '.' to listOf('.', '.'),
            '@' to listOf('@', '.')
        )

        val grid = rawGrid.split("\n")
            .map { line -> line.flatMap { c -> gridMapping[c] ?: error("Unsupported tile: $c") }.toMutableList() }

        val moves = rawMoves.split("\n").asSequence()
            .map { line -> line.map { c -> Direction.from(c) } }
            .flatten()
            .toList()

        fun moveRobot(start: Cell) {
            var robot = start
            for (d in moves) {
                val movesToMake = mutableListOf<Cell>(robot)
                val seen = mutableSetOf<Cell>()

                var canMove = true
                var i = 0
                while (i < movesToMake.size) {
                    val curr = movesToMake[i++]
                    val next = curr.move(d)

                    if (next in seen) {
                        continue
                    }

                    when (grid[next.row][next.col]) {
                        '#' -> {
                            canMove = false
                            break
                        }

                        '.' -> continue

                        '[' -> {
                            movesToMake += next.also { seen += it }
                            movesToMake += next.move(Direction.RIGHT).also { seen += it }
                        }

                        ']' -> {
                            movesToMake += next.also { seen += it }
                            movesToMake += next.move(Direction.LEFT).also { seen += it }
                        }

                        else -> error("Unexpected char at (${next.row}, ${next.col}) = ${grid[next.row][next.col]}")
                    }
                }

                if (!canMove) {
                    continue
                }

                for ((row, col) in movesToMake.asReversed()) {
                    grid[row + d.dr][col + d.dc] = grid[row][col]
                    grid[row][col] = '.'
                }

                // update the robot's current position
                robot = robot.move(d)
            }
        }

        val robot = findRobot(grid)
        moveRobot(robot)
        return sumBoxes(grid, '[')
    }

    // test if implementation meets criteria from the description, like:
    check(part1(readInputAsString("day$DAY_ID/Day${DAY_ID}_test1")) == 10092)
    check(part1(readInputAsString("day$DAY_ID/Day${DAY_ID}_test2")) == 2028)

    check(part2(readInputAsString("day$DAY_ID/Day${DAY_ID}_test1")) == 9021)

    val input = readInputAsString("day$DAY_ID/Day$DAY_ID")
    part1(input).println() // answer 1436690
    part2(input).println() // answer 1482350
}
