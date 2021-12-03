package day03

import readInput

fun part2Helper(input: List<String>, condition1: Char, condition2: Char): Int {
    val curList = mutableListOf<String>()
    val prevList = mutableListOf<String>()
    prevList.addAll(input)

    for (col in 0 until input[0].length) {
        val zeros = prevList.count { it[col] == '0' }
        val ones = prevList.count { it[col] == '1' }

        curList.clear()
        curList.addAll(prevList.filter { it[col] == (if (ones >= zeros) condition1 else condition2) })

        prevList.clear()
        prevList.addAll(curList)

        if (curList.size == 1) {
            break
        }
    }

    return curList[0].toInt(2)
}

fun main() {
    fun part1(input: List<String>): Int {
        var gamma = ""
        var epsilon = ""

        for (col in 0 until input[0].length) {
            val zeros = input.count { it[col] == '0' }
            val ones = input.count { it[col] == '1' }

            gamma += if (ones > zeros) '1' else '0'
            epsilon += if (ones >= zeros) '0' else '1'
        }

        return gamma.toInt(2) * epsilon.toInt(2)
    }

    fun part2(input: List<String>): Int {
        val oxygen = part2Helper(input, '1', '0')
        val co2 = part2Helper(input, '0', '1')

        return oxygen * co2
    }

    // test if implementation meets criteria from the description, like:
    //val testInput = readInput("dayX/test")
    //check(part1(testInput) == 1)
    //check(part2(testInput) == 1)

    val input = readInput("day03/input")
    println(part1(input))
    println(part2(input))
    // correct: 3885894, 4375225
}
