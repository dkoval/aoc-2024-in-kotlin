package day21

import println
import readInput
import java.util.*

private const val DAY_ID = "21"

private enum class Direction(val dr: Int, val dc: Int, val symbol: Char) {
    UP(-1, 0, '^'), DOWN(1, 0, 'V'), LEFT(0, -1, '<'), RIGHT(0, 1, '>')
}

fun main() {
    data class Cell(val row: Int, val col: Int) {
        fun move(d: Direction): Cell = Cell(row + d.dr, col + d.dc)
    }

    val numKeypad = mapOf(
        Cell(0, 0) to '7',
        Cell(0, 1) to '8',
        Cell(0, 2) to '9',
        Cell(1, 0) to '4',
        Cell(1, 1) to '5',
        Cell(1, 2) to '6',
        Cell(2, 0) to '1',
        Cell(2, 1) to '2',
        Cell(2, 2) to '3',
        Cell(3, 1) to '0',
        Cell(3, 2) to 'A'
    )

    val dirKeypad = mapOf(
        Cell(0, 1) to '^',
        Cell(0, 2) to 'A',
        Cell(1, 0) to '<',
        Cell(1, 1) to 'V',
        Cell(1, 2) to '>'
    )

    fun <T> cartesianProduct(lists: List<List<T>>): List<List<T>> = when (lists.size) {
        0 -> emptyList()
        1 -> lists
        else -> lists.fold(listOf(listOf())) { acc, list ->
            acc.flatMap { x -> list.map { y -> x + y } }
        }
    }

    fun solve(input: List<String>, numRobots: Int): Long {
        fun keypadPaths(keypad: Map<Cell, Char>, numRows: Int, numCols: Int): Map<Pair<Char, Char>, List<String>> {
            fun bfs(source: Cell, target: Cell): List<String> {
                if (source == target) {
                    return listOf("A")
                }

                val q = ArrayDeque<Pair<Cell, String>>().apply { offer(source to "") }
                val paths = mutableListOf<String>()
                // given 4-directional moves, the length of the longest possible path between any 2 cells = R + C - 2
                // -2 excludes 'A' rows- and column-wise
                var bestLength = numRows + numCols - 2
                while (q.isNotEmpty()) {
                    val (curr, path) = q.poll()
                    for (d in Direction.entries) {
                        val next = curr.move(d)

                        if (next !in keypad) {
                            continue
                        }

                        if (path.length + 1 > bestLength) {
                            return paths
                        }

                        if (next == target) {
                            paths += path + d.symbol + 'A'
                            bestLength = minOf(bestLength, path.length + 2)
                        } else {
                            q.offer(next to path + d.symbol)
                        }
                    }
                }
                return paths
            }

            val paths = mutableMapOf<Pair<Char, Char>, List<String>>()
            for (source in keypad.keys) {
                for (target in keypad.keys) {
                    paths[keypad[source]!! to keypad[target]!!] = bfs(source, target)
                }
            }
            return paths
        }

        // precompute all possible paths between any pair of keys on the keypad
        val numKeypadPaths = keypadPaths(numKeypad, 4, 3)
        val dirKeypadPaths = keypadPaths(dirKeypad, 2, 3)

        fun enterCode(code: String): List<String> {
            val paths = "A$code".zip(code).map { (k1, k2) -> numKeypadPaths[k1 to k2]!! }
            return cartesianProduct(paths).map { it.joinToString("") }
        }

        fun calcLength(path: String, numRobots: Int): Long {
            val cache = mutableMapOf<Pair<String, Int>, Long>()
            fun calc(path: String, numRobots: Int): Long {
                if (numRobots == 0) {
                    return path.length.toLong()
                }

                // already solved?
                val key = path to numRobots
                if (key in cache) {
                    return cache[key]!!
                }

                var length = 0L
                for ((k1, k2) in "A$path".zip(path)) {
                    length += dirKeypadPaths[k1 to k2]!!.minOf { calc(it, numRobots - 1) }
                }
                return length.also { cache[key] = it }
            }

            return calc(path, numRobots)
        }

        return input.sumOf { code ->
            var bestLength = Long.MAX_VALUE
            for (path in enterCode(code)) {
                bestLength = minOf(bestLength, calcLength(path, numRobots))
            }
            bestLength * code.dropLast(1).toInt()
        }
    }

    fun part1(input: List<String>): Long = solve(input, 2)

    fun part2(input: List<String>): Long = solve(input, 25)

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day$DAY_ID/Day${DAY_ID}_test")
    check(part1(testInput) == 126384L)

    val input = readInput("day$DAY_ID/Day$DAY_ID")
    part1(input).println() // answer 224326
    part2(input).println() // answer 279638326609472
}
