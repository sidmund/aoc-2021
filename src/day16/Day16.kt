package day16

import measure
import readInput
import showtime

private const val DAY = 16

private val convert = mapOf(
    '0' to "0000",
    '1' to "0001",
    '2' to "0010",
    '3' to "0011",
    '4' to "0100",
    '5' to "0101",
    '6' to "0110",
    '7' to "0111",
    '8' to "1000",
    '9' to "1001",
    'A' to "1010",
    'B' to "1011",
    'C' to "1100",
    'D' to "1101",
    'E' to "1110",
    'F' to "1111"
)

private fun String.toBinary(): String = fold("") { code, c -> code + convert[c] }

private var versionSum = 0

private fun operate(type: Int, values: List<Long>): Long =
    when (type) {
        1 -> values.fold(1) { mult, x -> mult * x }
        2 -> values.minOf { it }
        3 -> values.maxOf { it }
        5 -> if (values[0] > values[1]) 1 else 0
        6 -> if (values[0] < values[1]) 1 else 0
        7 -> if (values[0] == values[1]) 1 else 0
        else -> values.sum()
    }

private fun parse(packet: String): Pair<String, Long> {
    var input = packet

    versionSum += input.take(3).toInt(2)
    input = input.drop(3)

    val t = input.take(3).toInt(2)
    input = input.drop(3)

    if (t == 4) {
        var nr = ""
        do {
            val next = input.take(5)
            nr += next.substring(1)
            input = input.drop(5)
        } while (next[0] != '0')

        return Pair(input, nr.toLong(2))
    } else {
        val i = input.take(1)[0]
        input = input.drop(1)
        if (i == '0') {
            // Total length of all subpackets
            val l = input.take(15).toInt(2)
            input = input.drop(15)

            var subpackets = input.take(l)
            val values = mutableListOf<Long>()
            while (subpackets != "") {
                val received = parse(subpackets)
                subpackets = received.first
                values.add(received.second)
            }

            return Pair(input.drop(l), operate(t, values))
        } else {
            // Number of subpackets
            val l = input.take(11).toInt(2)
            input = input.drop(11)

            val values = mutableListOf<Long>()
            for (s in 1..l) {
                val received = parse(input)
                input = received.first
                values.add(received.second)
            }

            return Pair(input, operate(t, values))
        }
    }
}

fun main() {
    val files = setOf(
        Triple("input", 847, 333794664059)
    )

    files.forEach { (file, ans1, ans2) ->
        println("=== $file ===")

        val input = readInput("day$DAY/$file")[0].toBinary()

        val part1 = measure({ print("[ ${showtime(it)} ]") }) {
            val test = listOf(
                "D2FE28" to 6,
                "38006F45291200" to 9,
                "EE00D40C823060" to 14,
                "8A004A801A8002F478" to 16,
                "620080001611562C8802118E34" to 12,
                "C0015000016115A2E0802F182340" to 23,
                "A0016C880162017C3686B18A3D4780" to 31
            )

            for ((t, ans) in test) {
                versionSum = 0
                parse(t.toBinary())
                println("actual $versionSum, expected $ans")
            }

            versionSum = 0
            parse(input)
            versionSum
        }
        println(" expected: $ans1, actual: $part1")

        val part2 = measure({ print("[ ${showtime(it)} ]") }) {
            val test = listOf(
                "C200B40A82" to 3,
                "04005AC33890" to 54,
                "880086C3E88112" to 7,
                "CE00C43D881120" to 9,
                "D8005AC2A8F0" to 1,
                "F600BC2D8F" to 0,
                "9C005AC2F8F0" to 0,
                "9C0141080250320F1802104A08" to 1
            )

            for ((t, ans) in test) {
                println("actual ${parse(t.toBinary()).second}, expected $ans")
            }

            parse(input).second
        }
        println(" expected: $ans2, actual: $part2\n")
    }
}
