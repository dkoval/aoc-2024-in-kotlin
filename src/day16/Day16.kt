package day16

import println
import readInput
import java.util.*

private const val DAY_ID = "16"

private enum class Direction(val dx: Int, val dy: Int) {
    NORTH(-1, 0) {
        override fun neighbors(): List<Direction> = listOf(WEST, EAST)
    },

    SOUTH(1, 0) {
        override fun neighbors(): List<Direction> = listOf(EAST, WEST)
    },

    WEST(0, -1) {
        override fun neighbors(): List<Direction> = listOf(SOUTH, NORTH)
    },

    EAST(0, 1) {
        override fun neighbors(): List<Direction> = listOf(NORTH, SOUTH)
    };

    abstract fun neighbors(): List<Direction>
}

fun main() {
    data class Cell(val row: Int, val col: Int) {
        fun move(d: Direction): Cell = Cell(row + d.dx, col + d.dy)
    }

    data class TraversalInfo(val cell: Cell, val d: Direction, val cost: Int)

    fun part1(input: List<String>): Int {
        val grid = input.map { it.toMutableList() }
        val m = grid.size
        val n = grid[0].size

        fun traverse(start: Cell): Int {
            // Dijkstra's algorithm to find the shortest path between 2 cells
            val q = PriorityQueue<TraversalInfo>(compareBy { it.cost })
            val visited = mutableSetOf<Pair<Cell, Direction>>()

            q.offer(TraversalInfo(start, Direction.EAST, 0))
            while (q.isNotEmpty()) {
                val (curr, d, cost) = q.poll()
                visited += curr to d

                // reached the ending point?
                if (grid[curr.row][curr.col] == 'E') {
                    return cost
                }

                // move in the current direction
                var next = curr.move(d)
                if ((next to d) !in visited && grid[next.row][next.col] != '#') {
                    q.offer(TraversalInfo(next, d, cost + 1))
                }

                // rotate clockwise or counterclockwise 90 degrees
                for (nd in d.neighbors()) {
                    // only direction changes!!!
                    next = curr.move(nd)
                    if ((curr to nd) !in visited && grid[next.row][next.col] != '#') {
                        q.offer(TraversalInfo(curr, nd, cost + 1000))
                    }
                }
            }
            error("Couldn't reach the ending point")
        }

        for (row in 0 until m) {
            for (col in 0 until n) {
                if (grid[row][col] == 'S') {
                    return traverse(Cell(row, col))
                }
            }
        }
        error("Couldn't find the starting point")
    }

    fun part2(input: List<String>): Int {
        val grid = input.map { it.toMutableList() }
        val m = grid.size
        val n = grid[0].size

        fun traverse(start: Cell): Int {
            // Dijkstra's algorithm to find the shortest path between 2 cells
            val q = PriorityQueue<Pair<TraversalInfo, Set<Cell>>>(compareBy { (info, _) -> info.cost })
            val visited = mutableSetOf<Pair<Cell, Direction>>()

            q.offer(TraversalInfo(start, Direction.EAST, 0) to setOf(start))
            var bestCost = Int.MAX_VALUE
            val uniq = mutableSetOf<Cell>()
            while (q.isNotEmpty()) {
                val (info, path) = q.poll()
                val (curr, d, cost) = info
                visited += curr to d

                // reached the ending point?
                if (grid[curr.row][curr.col] == 'E') {
                    bestCost = minOf(bestCost, cost)
                    if (cost == bestCost) {
                        uniq += path
                    }
                }

                // move in the current direction
                var next = curr.move(d)
                if ((next to d) !in visited && grid[next.row][next.col] != '#') {
                    q.offer(TraversalInfo(next, d, cost + 1) to path + next)
                }

                // rotate clockwise or counterclockwise 90 degrees
                for (nd in d.neighbors()) {
                    // only direction changes!!!
                    var next = curr.move(nd)
                    if ((curr to nd) !in visited && grid[next.row][next.col] != '#') {
                        q.offer(TraversalInfo(curr, nd, cost + 1000) to path)
                    }
                }
            }
            return uniq.size
        }

        for (row in 0 until m) {
            for (col in 0 until n) {
                if (grid[row][col] == 'S') {
                    return traverse(Cell(row, col))
                }
            }
        }
        error("Couldn't find the starting/ending point")
    }

    // test if implementation meets criteria from the description, like:
    check(part1(readInput("day$DAY_ID/Day${DAY_ID}_test1")) == 7036)
    check(part1(readInput("day$DAY_ID/Day${DAY_ID}_test2")) == 11048)

    check(part2(readInput("day$DAY_ID/Day${DAY_ID}_test1")) == 45)
    check(part2(readInput("day$DAY_ID/Day${DAY_ID}_test2")) == 64)

    val input = readInput("day$DAY_ID/Day$DAY_ID")
    part1(input).println() // answer 109516
    part2(input).println() // answer 568
}
