package day18

import println
import readInput
import java.util.*

private const val DAY_ID = "18"

fun main() {
    // (x, y)
    val directions = arrayOf(
        // up
        intArrayOf(0, -1),
        // down
        intArrayOf(0, 1),
        // left
        intArrayOf(-1, 0),
        // right
        intArrayOf(1, 0),
    )

    data class Point(val x: Int, val y: Int) {
        fun move(dx: Int, dy: Int): Point = Point(x + dx, y + dy)
        fun isInBounds(n: Int): Boolean = x in 0..n && y in 0..n
    }

    fun part1(input: List<String>, n: Int, bytes: Int): Int {
        val corrupted = input.asSequence()
            .take(bytes)
            .map { line ->
                val (x, y) = line.split(",").map { it.toInt() }
                Point(x, y)
            }
            .toSet()

        val source = Point(0, 0)
        val target = Point(n, n)

        // BFS
        val q = ArrayDeque<Point>().apply { offer(source) }
        val visited = mutableSetOf<Point>(source)
        var steps = 0
        while (q.isNotEmpty()) {
            repeat(q.size) {
                val curr = q.poll()
                for ((dx, dy) in directions) {
                    val next = curr.move(dx, dy)

                    if (next == target) {
                        return steps + 1
                    }

                    if (next.isInBounds(n) && next !in corrupted && next !in visited) {
                        q.offer(next)
                        visited += next
                    }
                }
            }
            steps++
        }
        return error("Couldn't reach the target: $target")
    }

    fun part2(input: List<String>, n: Int): Point {
        val points = input.asSequence()
            .map { line ->
                val (x, y) = line.split(",").map { it.toInt() }
                Point(x, y)
            }

        val source = Point(0, 0)
        val target = Point(n, n)
        val corrupted = mutableSetOf<Point>()

        fun canReachTarget(): Boolean {
            // BFS
            val q = ArrayDeque<Point>().apply { offer(source) }
            val visited = mutableSetOf<Point>(source)

            while (q.isNotEmpty()) {
                val curr = q.poll()
                for ((dx, dy) in directions) {
                    val next = curr.move(dx, dy)

                    if (next == target) {
                        return true
                    }

                    if (next.isInBounds(n) && next !in corrupted && next !in visited) {
                        q.offer(next)
                        visited += next
                    }
                }
            }
            return false
        }

        for (point in points) {
            corrupted += point
            if (!canReachTarget()) {
                return point
            }
        }
        error("Couldn't find the blocking byte")
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day$DAY_ID/Day${DAY_ID}_test")
    check(part1(testInput, 6, 12) == 22)
    check(part2(testInput, 6) == Point(6, 1))

    val input = readInput("day$DAY_ID/Day$DAY_ID")
    part1(input, 70, 1024).println() // answer 506
    part2(input, 70).println() // answer 62,6
}
