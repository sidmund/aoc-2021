package aoc2020

import readInput

data class PasswordPolicy(
    val password: String,
    val range: IntRange,
    val letter: Char
) {
    fun validatePart1() = password.count { it == letter } in range

    fun validatePart2() =
        (password[range.first - 1] == letter) xor (password[range.last - 1] == letter)

    companion object {
        // Raw string for regex, escaping '\' not needed
        private val regex = Regex("""(\d+)-(\d+) ([a-z]): ([a-z]+)""")

        fun parseRegex(line: String): PasswordPolicy? =
            regex.matchEntire(line)
                ?.destructured
                ?.let { (start, end, letter, password) ->
                    PasswordPolicy(password, start.toInt()..end.toInt(), letter.single())
                }

        fun parse(line: String) = PasswordPolicy(
            password = line.substringAfter(": "),
            letter = line.substringAfter(" ").substringBefore(":").single(),
            range = line.substringBefore(" ").let {
                val (start, end) = it.split("-")
                start.toInt()..end.toInt()
            }
        )
    }
}

fun main() {
    val input = readInput("aoc2020/day02")
    val passwords = input.map(PasswordPolicy::parseRegex)
    println(passwords.count { it!!.validatePart1() })
    println(passwords.count { it!!.validatePart2() })
}