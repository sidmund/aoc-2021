package day07

import readInput
import kotlin.math.abs

fun fuelCost(input: List<Int>): Int {
    val maxPos = input.maxOrNull() ?: 0
    return (0..maxPos).minOf { pos ->
        (input.indices).sumOf { i -> abs(input[i] - pos) }
    }
}

fun fuelCostRate(input: List<Int>): Int {
    val maxPos = input.maxOrNull() ?: 0
    return (0..maxPos).minOf { pos ->
        (input.indices).sumOf { i -> (abs(input[i] - pos) * (abs(input[i] - pos) + 1)) / 2 }
    }
}

fun main() {
    val test = readInput("day07/test")[0].split(",").map(String::toInt)
    val input = readInput("day07/input")[0].split(",").map(String::toInt)

    check(fuelCost(test) == 37)
    check(fuelCostRate(test) == 168)

    val cost = fuelCost(input)
    val costRate = fuelCostRate(input)
    check(cost == 348996)
    check(costRate == 98231647)

    println("Part 1: constant fuel cost, lowest cost is $cost")
    println("Part 2: increasing fuel cost, lowest cost is $costRate")
}