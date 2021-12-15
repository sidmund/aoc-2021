import java.io.File
import java.math.BigInteger
import java.security.MessageDigest

/**
 * Reads lines from the given input txt file.
 */
fun readInput(filename: String) = File("src", "$filename.txt").readLines()

/**
 * Reads lines from the given input txt file, converting them to Int.
 */
fun readInputAsInt(filename: String) = File("src", "$filename.txt").readLines().map(String::toInt)

/**
 * Reads lines for given input txt file, converting them to an 2-dimensional array of integers.
 * Each line should be of equal length, and the file should only contain integers.
 * Example input:
 * """
 * 2145364575
 * 4690259237
 * 4906890466
 * """
 */
fun readTo2DArray(filename: String): Array<Array<Int>> = readInput(filename)
    .map { it.toCharArray().map(Char::digitToInt).toTypedArray() }.toTypedArray()

/**
 * Converts string to md5 hash.
 */
fun String.md5(): String = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray())).toString(16)

/**
 * Measure execution time in ns of supplied function.
 * @param logger a lambda expression to further process the elapsed time
 * @param function the function to measure the time of
 * @return the result of function
 */
inline fun <T> measure(logger: (Long) -> Unit, function: () -> T): T {
    val start = System.nanoTime()
    val result: T = function.invoke()
    logger.invoke(System.nanoTime() - start)
    return result
}

/**
 * Convert a time in ns to a more sensible format for a human reader.
 */
fun showtime(ns: Long, decimals: Int = 1): String = when {
    ns < 1e3 -> "${"%${4 + decimals}d".format(ns)} ns"
    ns < 1e6 -> "${"%${4 + decimals}.${decimals}f".format(ns / 1e3)} us"
    ns < 1e9 -> "${"%${4 + decimals}.${decimals}f".format(ns / 1e6)} ms"
    else -> "${"%${4 + decimals}.${decimals}f".format(ns / 1e9)}  s"
}
