package day08

import println
import readInput

private const val DAY_ID = "08"

fun main() {
    data class Point(val x: Int, val y: Int) {
        fun isInBounds(m: Int, n: Int): Boolean = x in 0 until m && y in 0 until n
    }

    fun parseGrid(input: List<String>): List<List<Char>> = input.map { it.toList() }

    fun groupAntennasByFrequency(grid: List<List<Char>>): Map<Char, List<Point>> {
        val m = grid.size
        val n = grid[0].size

        val res = mutableMapOf<Char, MutableList<Point>>()
        for (x in 0 until m) {
            for (y in 0 until n) {
                if (grid[x][y] == '.') {
                    continue
                }
                res.getOrPut(grid[x][y]) { mutableListOf() } += Point(x, y)
            }
        }
        return res
    }

    fun part1(input: List<String>): Int {
        val grid = parseGrid(input)
        val m = grid.size
        val n = grid[0].size

        val antinodes = mutableSetOf<Point>()
        for (antennas in groupAntennasByFrequency(grid).values) {
            for (i in 0 until antennas.size - 1) {
                for (j in i + 1 until antennas.size) {
                    val (x1, y1) = antennas[i]
                    val (x2, y2) = antennas[j]

                    val dx = x2 - x1
                    val dy = y2 - y1

                    antinodes += sequenceOf(
                        Point(x1 - dx, y1 - dy),
                        Point(x2 + dx, y2 + dy)
                    ).filter { it.isInBounds(m, n) }
                }
            }
        }
        return antinodes.size
    }

    fun part2(input: List<String>): Int {
        val grid = parseGrid(input)
        val m = grid.size
        val n = grid[0].size

        val antinodes = mutableSetOf<Point>()
        for (antennas in groupAntennasByFrequency(grid).values) {
            for (i in 0 until antennas.size - 1) {
                for (j in i + 1 until antennas.size) {
                    val (x1, y1) = antennas[i]
                    val (x2, y2) = antennas[j]

                    // Collect all points that lie on the straight line going through (x1, y1) and (x2, y2).
                    // Equation of a straight line through 2 points:
                    // (x - x1) / (x2 - x1) = (y - y1) / (y2 - y1)
                    for (x in 0 until m) {
                        for (y in 0 until n) {
                            if ((x - x1) * (y2 - y1) == (y - y1) * (x2 - x1)) {
                                antinodes += Point(x, y)
                            }
                        }
                    }
                }
            }
        }
        return antinodes.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day$DAY_ID/Day${DAY_ID}_test")
    check(part1(testInput) == 14)
    check(part2(testInput) == 34)

    val input = readInput("day$DAY_ID/Day$DAY_ID")
    part1(input).println() // answer 291
    part2(input).println() // answer 1015
}
