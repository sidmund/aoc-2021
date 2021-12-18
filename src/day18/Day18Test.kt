package day18

fun testExplode() {
    println("TEST EXPLODE")

    val explodeTests = listOf(
        "[[[[[9,8],1],2],3],4]",
        "[7,[6,[5,[4,[3,2]]]]]",
        "[[6,[5,[4,[3,2]]]],1]",
        "[[3,[2,[1,[7,3]]]],[6,[5,[4,[3,2]]]]]",
        "[[3,[2,[8,0]]],[9,[5,[4,[3,2]]]]]"
    ).map(Snailfish::convert)

    for (t in explodeTests) {
        var index = t.canExplode()
        println("Explode: $t")
        while (index != -1) {
            t.explodeAt(index)
            index = t.canExplode()
        }
        println(t)
    }

    println()
}

fun testSplit() {
    println("TEST SPLIT")

    val splitTests = listOf(
        Snailfish(mutableListOf("[", "13", ",", "9", "]")),
        Snailfish(
            mutableListOf(
                "[",
                "[",
                "5",
                ",",
                "8",
                "]",
                ",",
                "[",
                "6",
                ",",
                "[",
                "25",
                ",",
                "1",
                "]",
                "]",
                "]"
            )
        ),
    )

    for (t in splitTests) {
        var index = t.canSplit()
        println("Split: $t")
        while (index != -1) {
            t.splitAt(index)
            index = t.canSplit()
        }
        println(t)
    }

    println()
}

fun testAddition() {
    println("TEST ADDITION")

    val test1 = listOf(
        "[[[[4,3],4],4],[7,[[8,4],9]]]",
        "[1,1]"
    ).map(Snailfish::convert)
    println(test1[0] snail test1[1])

    val test2 = listOf(
        "[[[0,[4,5]],[0,0]],[[[4,5],[2,6]],[9,5]]]",
        "[7,[[[3,7],[4,3]],[[6,3],[8,8]]]]",
        "[[2,[[0,8],[3,4]]],[[[6,7],1],[7,[1,6]]]]",
        "[[[[2,4],7],[6,[0,5]]],[[[6,8],[2,8]],[[2,1],[4,5]]]]",
        "[7,[5,[[3,8],[1,4]]]]",
        "[[2,[2,2]],[8,[8,1]]]",
        "[2,9]",
        "[1,[[[9,3],9],[[9,0],[0,7]]]]",
        "[[[5,[7,4]],7],1]",
        "[[[[4,2],2],6],[8,7]]"
    ).map(Snailfish::convert)
    var snail = test2[0] snail test2[1]
    for (i in 2 until test2.size) {
        snail = snail snail test2[i]
    }
    println(snail)

    println()
}

fun testMagnitude() {
    println("TEST MAGNITUDE")

    val tests = listOf(
        "[[1,2],[[3,4],5]]",
        "[[[[0,7],4],[[7,8],[6,0]]],[8,1]]",
        "[[[[1,1],[2,2]],[3,3]],[4,4]]",
        "[[[[3,0],[5,3]],[4,4]],[5,5]]",
        "[[[[5,0],[7,4]],[5,5]],[6,6]]",
        "[[[[8,7],[7,7]],[[8,6],[7,7]]],[[[0,7],[6,6]],[8,7]]]"
    ).map(Snailfish::convert)
    val ans = listOf(143, 1384, 445, 791, 1137, 3488)

    for ((i, t) in tests.withIndex()) {
        println("Actual: ${t.magnitude()}, expected: ${ans[i]}")
    }

    println()
}
