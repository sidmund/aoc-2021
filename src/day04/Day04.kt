package day04

import readInput

const val BOARD_SIZE = 5

fun main() {
    val input = readInput("day04/input")

    val drawn = input[0].split(",").map(String::toInt)

    val boardMatrix = input.drop(1).flatMap { it.split(" ") }.filter { it != "" }.map(String::toInt).chunked(25)
    val nrBoards = boardMatrix.size

    val boards = Array<Board>(nrBoards) { Board(BOARD_SIZE) }
    for ((i, board) in boards.withIndex()) {
        board.populate(boardMatrix[i])
    }

    val wonBoards = Array<Boolean>(nrBoards) { false }
    for (d in drawn) {
        for ((i, board) in boards.withIndex()) {
            if (!wonBoards[i]) {
                board.mark(d)
                if (board.bingo()) {
                    wonBoards[i] = true
                    when {
                        wonBoards.count { it } == 1 -> {
                            println("(Part 1) Board $i won first, final score: ${board.score() * d}")
                        }
                        wonBoards.reduce { acc, won -> acc && won } -> {
                            println("(Part 2) Board $i won last, final score: ${board.score() * d}")
                        }
                    }
                }
            }
        }
    }
}