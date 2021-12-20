package day20

import measure
import readInput
import showtime

private const val DAY = 20

private fun picture(image: Array<Array<Boolean>>) {
    println("== Image ==")
    for (y in image.indices) {
        for (x in 0 until image[0].size) {
            print(if (image[y][x]) '#' else '.')
        }
        println()
    }
    println()
}

private fun enhance(
    img: Array<Array<Boolean>>, enhancer: Array<Boolean>, n: Int, compensate: Boolean
): Array<Array<Boolean>> {
    var curImg = img
    var expand = 2
    var expandHalf = 1

    for (gen in 1..n) {
        val h = curImg.size
        val w = curImg[0].size
        val imgPlus = Array(h + expand) { Array(w + expand) { false } }

        for (y in -expandHalf until h + expandHalf) {
            for (x in -expandHalf until w + expandHalf) {
                imgPlus[y + expandHalf][x + expandHalf] = pixelAt(curImg, w, h, x, y, enhancer, gen, compensate)
            }
        }

        curImg = imgPlus
        expand += 2
        expandHalf = expand / 2
    }
    return curImg
}

private fun pixelAt(
    img: Array<Array<Boolean>>, w: Int, h: Int, x: Int, y: Int, enhancer: Array<Boolean>, gen: Int, compensate: Boolean
): Boolean {
    var binary = ""
    for (r in (y - 1)..(y + 1)) {
        for (c in (x - 1)..(x + 1)) {
            binary +=
                if (r in 0 until h && c in 0 until w) {
                    if (img[r][c]) '1' else '0'
                } else {
                    if (compensate) {
                        if (gen % 2 == 1) '0' else '1'
                    } else {
                        '0'
                    }
                }
        }
    }
    return enhancer[binary.toInt(2)]
}

fun main() {
    val files = setOf(
        Triple("test", 35, 3351),
        Triple("input", 5259, 15287)
    )

    files.forEach { (file, ans1, ans2) ->
        println("=== $file ===")
        val input = readInput("day$DAY/$file")
        val enhancer = input[0].map { it == '#' }.toTypedArray()
        val image = input.drop(2).map { it.toCharArray().map { it == '#' }.toTypedArray() }.toTypedArray()

        // The test file doesn't have odd generations tricks, no compensation needed

        val part1 = measure({ print("[ ${showtime(it)} ]") }) {
            enhance(image, enhancer, 2, file != "test").flatten().count { it }
        }
        println(" expected: $ans1, actual: $part1")

        val part2 = measure({ print("[ ${showtime(it)} ]") }) {
            enhance(image, enhancer, 50, file != "test").flatten().count { it }
        }
        println(" expected: $ans2, actual: $part2\n")
    }
}
