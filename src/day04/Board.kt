package day04

/*
Bingo board of size x size
 */
class Board(private val size: Int) {

    data class Cell(var nr: Int, var marked: Boolean) {
        override fun toString(): String = "Cell: $nr ($marked)"
    }

    private val cells = List(size * size) { Cell(0, false) }

    // Fill the board with specified values. All the cells are left as unmarked.
    // Needs to be an int collection that is as long as board size^2.
    fun populate(ints: Collection<Int>): Board {
        if (ints.size != size * size) {
            println("Uh oh, the array needs to contain as many elements as a board can fit.")
        }

        for ((index, nr) in ints.withIndex()) {
            cells[index].nr = nr
        }
        return this
    }

    // Marks off specified nr if it's on this board, returns whether it was marked.
    // NB: the board should contain unique numbers, so this function will return after the first nr has been found
    fun mark(nr: Int): Boolean {
        for (cell in cells) {
            if (cell.nr == nr) {
                cell.marked = true
                return true
            }
        }
        return false
    }

    private fun containsMarkedRow(): Boolean {
        for (row in cells.chunked(size)) {
            var wholeRowMarked = true
            if (row.count { it.marked } != size) {
                wholeRowMarked = false
            }
            if (wholeRowMarked) {
                return true
            }
        }
        return false
    }

    private fun containsMarkedCol(): Boolean {
        val matrix = cells.chunked(size)
        for (col in 0 until size) {
            var wholeColMatches = true
            for (row in 0 until size) {
                if (!matrix[row][col].marked) {
                    wholeColMatches = false
                    break
                }
            }
            if (wholeColMatches) {
                return true
            }
        }
        return false
    }

    // Returns whether this board won, "Bingo!"
    fun bingo(): Boolean = containsMarkedRow() || containsMarkedCol()

    fun score(): Int = cells.filter { !it.marked }.sumOf { it.nr }

}
