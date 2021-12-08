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
 * Converts string to md5 hash.
 */
fun String.md5(): String = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray())).toString(16)

/**
 * Measure execution time in ms of supplied function.
 * @param logger a lambda expression to further process the elapsed time
 * @param function the function to measure the time of
 * @return the result of function
 */
inline fun <T> measure(logger: (Long) -> Unit, function: () -> T): T {
    val start = System.currentTimeMillis()
    val result: T = function.invoke()
    logger.invoke(System.currentTimeMillis() - start)
    return result
}
