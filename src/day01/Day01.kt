package day01

import readInput

fun part1(input: List<String>): Int {
    /*var count = 0
    for (index in input.indices.filter { it > 0 }) {
        if (input[index].toInt() > input[index - 1].toInt()) {
            count++
        }
    }
    return count
    */
    return input.indices.filter { it > 0 && input[it].toInt() > input[it - 1].toInt() }.size
}

fun part2(input: List<String>): Int {
    var count = 0
    var prevSum = 0
    for (index in input.indices) {
        val a = input[index].toInt()
        val b = if (index + 1 >= input.size) 0 else input[index + 1].toInt()
        val c = if (index + 2 >= input.size) 0 else input[index + 2].toInt()
        val sum = a + b + c
        if (prevSum != 0 && sum > prevSum) {
            count++
        }
        prevSum = sum
    }
    return count
}

fun main() {
    // test if implementation meets criteria from the description, like:
    //val testInput = readInput("day01/Day01_test")
    //check(part1(testInput) == 1)

    val input = readInput("day01/input")
    println(part1(input))
    println(part2(input))
}
