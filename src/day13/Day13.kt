package day13

import println
import readInputAsString

private const val DAY_ID = "13"

fun main() {
    data class Delta(val dx: Int, val dy: Int)
    data class Block(val a: Delta, val b: Delta, val x: Int, val y: Int)

    fun part1(input: String): Int {
        val equation = """Button [A|B]: X\+(\d+), Y\+(\d+)""".toRegex()
        val prize = """Prize: X=(\d+), Y=(\d+)""".toRegex()
        val threshold = 100

        val blocks = input.split("\n\n").map {
            val (l1, l2, l3) = it.split("\n")
            val (adx, ady) = equation.find(l1)!!.destructured
            val (bdx, bdy) = equation.find(l2)!!.destructured
            val (x, y) = prize.find(l3)!!.destructured
            Block(
                Delta(adx.toInt(), ady.toInt()),
                Delta(bdx.toInt(), bdy.toInt()),
                x.toInt(),
                y.toInt()
            )
        }

        fun solve(block: Block): Int {
            val (a, b, x, y) = block

            val varB = (x * a.dy - y * a.dx) / (b.dx * a.dy - a.dx * b.dy)
            val varA = (x - b.dx * varB) / a.dx

            // threshold
            if (varA > threshold || varB > threshold) {
                return 0
            }

            // validation
            if (a.dx * varA + b.dx * varB != x || a.dy * varA + b.dy * varB != y) {
                return 0
            }

            return 3 * varA + varB
        }

        return blocks.sumOf { solve(it) }
    }

    fun part2(input: String): Long {
        val equation = """Button [A|B]: X\+(\d+), Y\+(\d+)""".toRegex()
        val prize = """Prize: X=(\d+), Y=(\d+)""".toRegex()
        val extra = 10000000000000

        val blocks = input.split("\n\n").map {
            val (l1, l2, l3) = it.split("\n")
            val (adx, ady) = equation.find(l1)!!.destructured
            val (bdx, bdy) = equation.find(l2)!!.destructured
            val (x, y) = prize.find(l3)!!.destructured
            Block(
                Delta(adx.toInt(), ady.toInt()),
                Delta(bdx.toInt(), bdy.toInt()),
                x.toInt(),
                y.toInt()
            )
        }

        fun solve(block: Block): Long {
            val (a, b, _, _) = block
            val x = block.x + extra
            val y = block.y + extra

            val varB = (x * a.dy - y * a.dx) / (b.dx * a.dy - a.dx * b.dy)
            val varA = (x - b.dx * varB) / a.dx

            // validation
            if (a.dx * varA + b.dx * varB != x || a.dy * varA + b.dy * varB != y) {
                return 0
            }

            return 3 * varA + varB
        }

        return blocks.sumOf { solve(it) }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInputAsString("day$DAY_ID/Day${DAY_ID}_test")
    check(part1(testInput) == 480)

    val input = readInputAsString("day$DAY_ID/Day$DAY_ID")
    part1(input).println() // answer 25751
    part2(input).println() // answer 108528956728655
}
