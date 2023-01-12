data class Customer(
    val name: String,
    val age: Int,
    val country: String
) {
    companion object {
        private val regex = Regex("""([A-Za-z]+): ([A-Za-z0-9]+)""")

        fun parse(line: String): Customer? =
            regex.matchEntire(line)
                ?.destructured
                ?.let {
                        (n,a,c) ->
                    Customer(
                        n,
                        a.toInt(),
                        c
                    )
                }
    }
}

/*
Name: Bob
Age: 20
Country: Netherlands

Name: Alice
Age: 21
Country: Belgium
 */

data class Team(
    val name: String
) {
    companion object {
        private val regexTeam = Regex("""([A-Za-z ]+)""")
        private val regexMembers = Regex("""([a-z]+):(\d\d)""")

//        fun parse(line: String): Team? {
//            if (line.contains(":")) {
//                for (l in line.split(" ")) {
//                    // parse member
//                }
//            } else {
////                regexTeam.matchEntire(line)
////                    ?.destructured
////                    ?.let {
////
////                    }
//            }
//        }
    }
}

/*
The Wonderboys
carl:12 jonas:47 doug:38

The Fast Ladies
karen:51 taylor:21 alicia:28 billie:18
*/


fun parseBlock(lines: List<String>) {
    val customers = lines.filter { it != "" }.windowed(3, 3)
    for ((index, customer) in customers.withIndex()) {
        println("Customer $index: ${customer[0]}, ${customer[1]}, ${customer[2]}")
    }
}

fun main() {
    val customerList = listOf<String>("Name: Bob", "Age: 20", "Country: Netherlands", "", "Name: Alice", "Age: 21", "Country: Belgium")
    parseBlock(customerList)
}
