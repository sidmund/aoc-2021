package day08

import measure
import readInput

// Model for a 7-segment display
class Display(
    var t: Char = 'x',
    var tl: Char = 'x',
    var tr: Char = 'x',
    var m: Char = 'x',
    var bl: Char = 'x',
    var br: Char = 'x',
    var b: Char = 'x'
) {
    // Remove dummy value 'x', this function should only return what segments have been set so far
    fun values(): Set<Char> = setOf(t, tl, tr, m, bl, br, b) - 'x'

    // Return the set of decoded segments that make up 0-9
    private fun zero(): Set<Char> = setOf(t, tl, tr, bl, br, b) - 'x'
    private fun one(): Set<Char> = setOf(tr, br) - 'x'
    private fun two(): Set<Char> = setOf(t, tr, m, bl, b) - 'x'
    private fun three(): Set<Char> = setOf(t, tr, m, br, b) - 'x'
    private fun four(): Set<Char> = setOf(tl, tr, m, br) - 'x'
    private fun five(): Set<Char> = setOf(t, tl, m, br, b) - 'x'
    private fun six(): Set<Char> = setOf(t, tl, m, bl, br, b) - 'x'
    private fun seven(): Set<Char> = setOf(t, tr, br) - 'x'
    private fun eight(): Set<Char> = setOf(t, tl, tr, m, bl, br, b) - 'x'
    private fun nine(): Set<Char> = setOf(t, tl, tr, m, br, b) - 'x'

    fun digitOf(segments: Set<Char>): Char =
        when (segments) {
            zero() -> '0'
            one() -> '1'
            two() -> '2'
            three() -> '3'
            four() -> '4'
            five() -> '5'
            six() -> '6'
            seven() -> '7'
            eight() -> '8'
            nine() -> '9'
            else -> 'x'
        }
}

fun part1(outputs: List<List<String>>): Int = outputs.fold(0) { total, output ->
    total + output.count { it.length in arrayOf(2, 3, 4, 7) }
}

fun part2(input: List<Pair<List<String>, List<String>>>): Int = input.fold(0) { sum, code ->
    sum + decode(code.first, code.second)
}

fun decode(connections: List<String>, outputs: List<String>): Int {
    val display = Display()

    // Gather all codes by length
    var one = ""
    var seven = ""
    var four = ""
    var eight = ""
    val twoThreeFive = mutableListOf<String>()
    val zeroSixNine = mutableListOf<String>()
    for (signal in connections) {
        when (signal.length) {
            2 -> one = signal
            3 -> seven = signal
            4 -> four = signal
            7 -> eight = signal
            6 -> zeroSixNine.add(signal)
            5 -> twoThreeFive.add(signal)
        }
    }

    // The character in 7 that's not in 1 is the top
    display.t = seven.toSet().first { !one.contains(it) }

    // Those in 4 that are not in 1 (the top left and middle)
    val tlm = four.toSet().filter { !one.contains(it) }.toMutableSet()

    // Determine common characters in 0,6,9: t,b,tl,br
    val cmn069 = zeroSixNine[0].toSet()
        .intersect(zeroSixNine[1].toSet())
        .intersect(zeroSixNine[2].toSet())

    // Intersection of tlm with tmb gives the top left, and consequently the middle
    val topleft = cmn069.intersect(tlm).first()
    display.tl = topleft
    display.m = (tlm - topleft).first()

    // Use common between 0,6,9 and 1 to find bottom right
    val br = one.toSet().intersect(cmn069).first()
    display.br = br

    // The leftover character in one is the top right
    display.tr = (one.toSet() - br).first()

    // Common between 2,3,5: t,m,b. Filter out top and middle to get bottom
    display.b = twoThreeFive[0].toSet()
        .intersect(twoThreeFive[1].toSet())
        .intersect(twoThreeFive[2].toSet())
        .first { it != display.t && it != display.m }

    // One character is still not assigned
    display.bl = (eight.toSet() - display.values()).first()

    // Decode outputs as a 4-digit integer
    return outputs.fold("0") { digit, code -> digit + display.digitOf(code.toSet()) }.toInt()
}

fun main() {
    val testInput = readInput("day08/test").map { it.split(" | ") }
    val testCodes = testInput.indices.map { i ->
        testInput.map { it[0].split(" ") }[i] to testInput.map { it[1].split(" ") }[i]
    }

    val input = readInput("day08/input").map { it.split(" | ") }
    val codes = input.indices.map { i ->
        input.map { it[0].split(" ") }[i] to input.map { it[1].split(" ") }[i]
    }

    val part1 = measure({ println("Part 1 took $it ms") }) {
        check(part1(testCodes.map { it.second }) == 26)
        part1(codes.map { it.second }) // 476
    }
    println(" Answer: $part1")

    val part2 = measure({ println("Part 2 took $it ms") }) {
        check(part2(testCodes) == 61229)
        part2(codes) // 1011823
    }
    println(" Answer: $part2")
}
