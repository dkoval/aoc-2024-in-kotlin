package day09

import println
import readInputAsString

private const val DAY_ID = "09"

fun main() {
    fun part1(input: String): Long {
        val n = input.length

        val disk = mutableListOf<Int>()
        val spaces = mutableListOf<Int>()
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
                    spaces += disk.lastIndex
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

            val left = spaces[i]
            if (left >= right) {
                break
            }

            // occupy free space
            disk[left] = disk[right]
            disk[right] = -1
            right--
            i++
        }

        return disk.asSequence()
            .withIndex()
            .takeWhile { it.value >= 0 }
            .map { it.index * it.value.toLong() }
            .sum()
    }

    fun part2(input: String): Long {
        // file ID -> (file block offset on the disk, block size)
        val files = mutableMapOf<Int, Pair<Int, Int>>()
        // spaces[i] - (free space block offset on the disk, block size)
        val spaces = mutableListOf<Pair<Int, Int>>()

        var offset = 0
        for ((index, value) in input.withIndex()) {
            val blockSize = value.digitToInt()
            if (index % 2 == 0) {
                val fileId = index / 2
                files[fileId] = offset to blockSize
            } else {
                spaces += offset to blockSize
            }
            offset += blockSize
        }

        for (fileId in files.keys.reversed()) {
            val (fileOffset, fileBlockSize) = files[fileId]!!
            // check if there's a contiguous block of spaces to fit the entire file
            var i = 0
            while (i < spaces.size) {
                val (freeSpaceOffset, freeSpaceBlockSize) = spaces[i]
                if (freeSpaceOffset > fileOffset) {
                    break
                }
                if (freeSpaceBlockSize == fileBlockSize) {
                    // occupy the empty space
                    files[fileId] = freeSpaceOffset to fileBlockSize
                    spaces.removeAt(i) // TODO: can we do better?
                    break
                }
                if (freeSpaceBlockSize > fileBlockSize) {
                    // occupy the empty space + update the remaining extra space
                    files[fileId] = freeSpaceOffset to fileBlockSize
                    spaces[i] = freeSpaceOffset + fileBlockSize to freeSpaceBlockSize - fileBlockSize
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
