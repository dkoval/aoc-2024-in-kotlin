package day14

import println
import readInput

private const val DAY_ID = "14"

fun main() {
    data class Position(val x: Int, val y: Int)
    data class Velocity(val dx: Int, val dy: Int)
    data class Robot(val p: Position, val v: Velocity)

    fun part1(input: List<String>, width: Int, height: Int, seconds: Int): Int {
        val robots = input.map { line ->
            // Example: p=0,4 v=3,-3
            val (p, v) = line.split(" ")
            // p=0,4
            val (x, y) = p.drop(2).split(",")
            // v=3,-3
            val (dx, dy) = v.drop(2).split(",")

            Robot(
                Position(x.toInt(), y.toInt()),
                Velocity(dx.toInt(), dy.toInt())
            )
        }

        // count the number of robots in each quadrant
        val quadrants = IntArray(4)
        val midX = width / 2
        val midY = height / 2

        for ((p, v) in robots) {
            var nx = (p.x + v.dx * seconds) % width
            var ny = (p.y + v.dy * seconds) % height

            // wrap around the edges
            nx += if (nx < 0) width else 0
            ny += if (ny < 0) height else 0

            if (ny < midY) {
                if (nx < midX) {
                    quadrants[0]++
                } else if (nx > midX) {
                    quadrants[1]++
                }
            } else if (ny > midY) {
                if (nx < midX) {
                    quadrants[2]++
                } else if (nx > midX) {
                    quadrants[3]++
                }
            }
        }

        // calculate the safety factor
        return quadrants.fold(1) { acc, x -> acc * x }
    }

    fun part2(input: List<String>, width: Int, height: Int) {
        val robots = input.map { line ->
            // Example: p=0,4 v=3,-3
            val (p, v) = line.split(" ")
            // p=0,4
            val (x, y) = p.drop(2).split(",")
            // v=3,-3
            val (dx, dy) = v.drop(2).split(",")

            Robot(
                Position(x.toInt(), y.toInt()),
                Velocity(dx.toInt(), dy.toInt())
            )
        }

        // Look for: #....### pattern in the output to capture the  picture of a Christmas tree
        for (i in 7100 downTo 7050) {
            val snapshot = mutableSetOf<Position>()
            for ((p, v) in robots) {
                var nx = (p.x + v.dx * i) % width
                var ny = (p.y + v.dy * i) % height

                // wrap around the edges
                nx += if (nx < 0) width else 0
                ny += if (ny < 0) height else 0

                snapshot += Position(nx, ny)
            }

            // plot
            println("--- Seconds passed: $i")
            for (y in 0 until height) {
                for (x in 0 until width) {
                    val symbol = if (Position(x, y) in snapshot) '#' else '.'
                    print(symbol)
                }
                println()
            }
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day$DAY_ID/Day${DAY_ID}_test")
    check(part1(testInput, 11, 7, 100).println() == 12)

    val input = readInput("day$DAY_ID/Day$DAY_ID")
    part1(input, 101, 103, 100).println() // answer 224357412
    part2(input, 101, 103) // answer 7083
}
