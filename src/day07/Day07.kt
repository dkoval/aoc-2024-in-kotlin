package day07

import println
import readInput

private const val DAY_ID = "07"

fun main() {
    fun solve(input: List<String>, fs: List<(Long, Long) -> Long>): Long {
        val equations = input.map { line ->
            val (res, nums) = line.split(": ")
            res.toLong() to nums.split(" ").map { it.toLong() }
        }

        fun isGood(res: Long, nums: List<Long>): Boolean {
            fun eval(curr: Long, index: Int): Boolean {
                if (index == nums.size) {
                    return curr == res
                }
                return fs.any { f -> eval(f(curr, nums[index]), index + 1) }
            }

            return nums.isNotEmpty() && eval(nums[0], 1)
        }

        return equations.asSequence()
            .filter { (res, nums) -> isGood(res, nums) }
            .map { (res, _) -> res }
            .sum()
    }

    fun part1(input: List<String>): Long {
        return solve(
            input,
            listOf(
                { x, y -> x * y },
                { x, y -> x + y }
            )
        )
    }

    fun part2(input: List<String>): Long {
        return solve(
            input,
            listOf(
                { x, y -> x * y },
                { x, y -> x + y },
                { x, y -> (x.toString() + y.toString()).toLong() }
            )
        )
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day$DAY_ID/Day${DAY_ID}_test")
    check(part1(testInput) == 3749L)
    check(part2(testInput) == 11387L)

    val input = readInput("day$DAY_ID/Day$DAY_ID")
    part1(input).println() // answer 975671981569
    part2(input).println() // answer 223472064194845
}
