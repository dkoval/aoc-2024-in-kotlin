package day22

import println
import readInput
import java.util.*

private const val DAY_ID = "22"

fun main() {
    fun mix(x: Long, y: Long): Long = x xor y
    fun prune(x: Long): Long = x % 16777216
    fun step(x: Long, y: Long): Long = prune(mix(x, y))

    fun nextSecret(secret: Long): Long {
        var res = secret
        res = step(res * 64, res)
        res = step(res / 32, res)
        res = step(res * 2048, res)
        return res
    }

    fun part1(input: List<String>): Long {
        fun generateSecretNumber(initial: Long): Long {
            var secret = initial
            repeat(2000) {
                secret = nextSecret(secret)
            }
            return secret
        }

        return input.asSequence()
            .map { generateSecretNumber(it.toLong()) }
            .sum()
    }

    fun part2(input: List<String>): Int {
        fun generatePrices(initial: Int): List<Int> {
            var secret = initial.toLong()
            val prices = mutableListOf<Int>(initial % 10)
            repeat(2000) {
                secret = nextSecret(secret)
                prices += (secret % 10).toInt()
            }
            return prices
        }

        val eachBuyerPrices = input.map { generatePrices(it.toInt()) }

        // sequence of 4 price changes -> total price
        val totalPrices = mutableMapOf<List<Int>, Int>()
        for (prices in eachBuyerPrices) {
            // for each sequence of 4 price changes, record the maximum price
            val changes = ArrayDeque<Int>()
            // sequence of 4 price changes -> maximum price
            val maxPrices = mutableMapOf<List<Int>, Int>()
            for (i in 1 until prices.size) {
                changes.offerLast(prices[i] - prices[i - 1])
                if (changes.size >= 4) {
                    val seq = changes.toList()
                    if (seq !in maxPrices) {
                        maxPrices[seq] = maxOf(maxPrices[seq] ?: 0, prices[i])
                    }
                    changes.pollFirst()
                }
            }

            // for each sequence, update the total price
            for ((seq, price) in maxPrices) {
                totalPrices[seq] = (totalPrices[seq] ?: 0) + price
            }
        }

        return totalPrices.values.max()
    }

    // test if implementation meets criteria from the description, like:
    check(part1(readInput("day$DAY_ID/Day${DAY_ID}_part1_test")) == 37327623L)
    check(part2(readInput("day$DAY_ID/Day${DAY_ID}_part2_test")) == 23)

    val input = readInput("day$DAY_ID/Day$DAY_ID")
    part1(input).println() // answer 20441185092
    part2(input).println() // answer 2268
}
