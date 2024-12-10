package day09

import println
import readInputAsString

private const val DAY_ID = "09"

fun main() {
    fun part1(input: String): Long {
        val n = input.length

        val disk = mutableListOf<Int>()
        val freeSpaces = mutableListOf<Int>()
        for (i in 0 until n step 2) {
            // file
            repeat(input[i].digitToInt()) {
                val fileId = i / 2
                disk += fileId
            }
            // free space
            if (i + 1 < n) {
                repeat(input[i + 1].digitToInt()) {
                    disk += -1
                    freeSpaces += disk.lastIndex
                }
            }
        }

        // defragment the disk
        var i = 0
        var right = disk.lastIndex
        while (true) {
            // skip over free space from the right
            while (right >= 0 && disk[right] == -1) {
                right--
            }

            val left = freeSpaces[i]
            if (left >= right) {
                break
            }

            // occupy free space
            disk[left] = disk[right]
            disk[right] = -1
            right--
            i++ // proceed to the next free space
        }

        return disk.asSequence()
            .withIndex()
            .takeWhile { it.value >= 0 }
            .map { it.index * it.value.toLong() }
            .sum()
    }

    data class Block(val offset: Int, val size: Int)

    fun part2(input: String): Long {
        // file ID -> file block
        val files = mutableMapOf<Int, Block>()
        // freeSpaces[i] - the i-th free space block
        val freeSpaces = mutableListOf<Block>()

        var offset = 0
        for ((index, value) in input.withIndex()) {
            val size = value.digitToInt()
            if (index % 2 == 0) {
                val fileId = index / 2
                files[fileId] = Block(offset, size)
            } else {
                freeSpaces += Block(offset, size)
            }
            offset += size
        }

        // defragment the disk
        for (fileId in files.keys.reversed()) {
            val file = files[fileId]!!
            // check if there's a contiguous block of spaces to fit the entire file
            var i = 0
            while (i < freeSpaces.size) {
                val freeSpace = freeSpaces[i]
                if (freeSpace.offset > file.offset) {
                    break
                }
                if (freeSpace.size == file.size) {
                    // occupy the empty space
                    files[fileId] = Block(freeSpace.offset, file.size)
                    freeSpaces.removeAt(i) // TODO: can this improved?
                    break
                }
                if (freeSpace.size > file.size) {
                    // occupy the empty space + update the remaining extra space
                    files[fileId] = Block(freeSpace.offset, file.size)
                    freeSpaces[i] = Block(freeSpace.offset + file.size, freeSpace.size - file.size)
                    break
                }
                i++
            }
        }

        var checksum = 0L
        for ((fileId, value) in files) {
            val (offset, blockSize) = value
            for (i in offset until offset + blockSize) {
                checksum += i * fileId
            }
        }
        return checksum
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInputAsString("day$DAY_ID/Day${DAY_ID}_test")
    check(part1(testInput) == 1928L)
    check(part2(testInput) == 2858L)

    val input = readInputAsString("day$DAY_ID/Day$DAY_ID")
    part1(input).println() // answer 6259790630969
    part2(input).println() // answer 6289564433984
}
