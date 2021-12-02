package day01

import readInputAsInt

fun main() {
    fun part1(input: List<Int>): Int {
        /*var count = 0
        for (index in input.indices.filter { it > 0 }) {
            if (input[index].toInt() > input[index - 1].toInt()) {
                count++
            }
        }
        return count
        */

        // alternative solution
        println("using windowed: ${input.windowed(2).count { it[0] < it[1] }}")

        return input.indices.filter { it > 0 && input[it] > input[it - 1] }.size
    }

    fun part2(input: List<Int>): Int {
        var count = 0
        var prevSum = 0
        for (index in input.indices) {
            val a = input[index]
            val b = if (index + 1 >= input.size) 0 else input[index + 1]
            val c = if (index + 2 >= input.size) 0 else input[index + 2]
            val sum = a + b + c
            if (prevSum != 0 && sum > prevSum) {
                count++
            }
            prevSum = sum
        }

        // alternative solution
        println("using windowed: ${input.windowed(3).map{ it.sum() }.windowed(2).count { it[0] < it[1] }}")

        return count
    }

    // test if implementation meets criteria from the description, like:
    //val testInput = readInput("day01/Day01_test")
    //check(part1(testInput) == 1)

    val input = readInputAsInt("day01/input")
    println(part1(input))
    println(part2(input))
}
