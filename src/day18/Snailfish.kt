package day18

import kotlin.math.ceil
import kotlin.math.floor

class Snailfish(
    private val number: MutableList<String>
) {

    companion object {
        fun convert(string: String): Snailfish =
            Snailfish(string.split("").filter { it != "" }.toMutableList())
    }

    override fun toString(): String = number.joinToString("")

    // returns index of a bracket that is nested 4 deep (the fifth bracket), -1 if no more such exist
    fun canExplode(): Int {
        var depth = 0
        for ((index, c) in number.withIndex()) {
            if (c == "[") {
                depth++
                if (depth == 5) {
                    return index
                }
            } else if (c == "]") {
                depth--
            }
        }
        return -1
    }

    // return index of value that can be split, -1 if none
    fun canSplit(): Int {
        for (index in number.indices) {
            if (isValue(index) && valueAt(index) > 9) {
                return index
            }
        }
        return -1
    }

    // find index of last number before index, -1 if none
    private fun numberToLeft(index: Int): Int {
        var i = index
        while (i > -1) {
            if (isValue(i)) {
                break
            }
            i--
        }
        return i
    }

    // find index of first number after index, -1 if none
    private fun numberToRight(index: Int): Int {
        var i = index
        while (i < number.size) {
            if (isValue(i)) {
                return i
            }
            i++
        }
        return -1
    }

    private fun isValue(index: Int): Boolean =
        number.elementAt(index).matches(Regex("""(\d+)"""))

    private fun valueAt(index: Int): Int =
        if (isValue(index)) number[index].toInt() else -1

    private fun setValueAt(index: Int, value: Int) {
        number[index] = value.toString()
    }

    fun explodeAt(index: Int) {
        if (number.elementAt(index) == "["
            && number.elementAt(index + 4) == "]"
            && isValue(index + 1)
            && isValue(index + 3)
        ) {
            val left = valueAt(index + 1)
            val right = valueAt(index + 3)

            val leftNeighbor = numberToLeft(index)
            if (leftNeighbor != -1) {
                setValueAt(leftNeighbor, valueAt(leftNeighbor) + left)
            }

            val rightNeighbor = numberToRight(index + 5)
            if (rightNeighbor != -1) {
                setValueAt(rightNeighbor, valueAt(rightNeighbor) + right)
            }

            number[index] = "0"
            number.removeAt(index + 1)
            number.removeAt(index + 1)
            number.removeAt(index + 1)
            number.removeAt(index + 1)
        }
    }

    fun splitAt(index: Int) {
        val value = valueAt(index) / 2.0
        val left = floor(value).toInt().toString()
        val right = ceil(value).toInt().toString()

        number.removeAt(index)
        number.add(index, "]")
        number.add(index, right)
        number.add(index, ",")
        number.add(index, left)
        number.add(index, "[")
    }

    // "Snails" (adds) two Snailfish
    infix fun snail(other: Snailfish): Snailfish {
        val snailfish = convert("""[$this,$other]""")

        var explode = snailfish.canExplode()
        var split = snailfish.canSplit()
        while (explode != -1 || split != -1) {
            if (explode != -1) {
                snailfish.explodeAt(explode)
            } else {
                snailfish.splitAt(split)
            }
            explode = snailfish.canExplode()
            split = snailfish.canSplit()
        }

        return snailfish
    }

    fun magnitude(): Int {
        var m = 0
        while (number.size != 1) {
            for (i in 0 until (number.size - 2)) {
                // Only for pairs [a,b] the magnitude can be calculated
                if (isValue(i) && isValue(i + 2)) {
                    m = valueAt(i) * 3 + valueAt(i + 2) * 2
                    setValueAt(i - 1, m)
                    number.removeAt(i)
                    number.removeAt(i)
                    number.removeAt(i)
                    number.removeAt(i)
                    break
                }
            }
        }
        return m
    }
}
