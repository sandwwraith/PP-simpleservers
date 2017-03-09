package top.sandwwraith.simpleservers

import java.io.BufferedReader
import java.io.FileReader
import java.util.regex.Pattern

/**
 * @author Leonid Startsev
 *		  sandwwraith@gmail.com
 * 		  ITMO University, 2017
 **/

typealias ProcessTable = Map<Int, Pair<String, Int>>

class Router(filename: String = "process.cfg") {

    val table: ProcessTable

    init {

        val pattern = Pattern.compile("""^process\.(?<id>\d+)=(?<addr>.+):(?<port>\d{1,5})$""")

        fun parseLine(line: String): Pair<Int, Pair<String, Int>> {
            val m = pattern.matcher(line)
            if (!m.matches()) throw IllegalArgumentException("Line '$line' is not a valid config line")
            val id = m.group("id").toInt()
            val addr = m.group("addr")
            val port = m.group("port").toInt()
            if (port !in (1..65535)) throw IllegalArgumentException("Invalid port number $port")
            return id to (addr to port)
        }

        table = BufferedReader(FileReader(filename)).useLines { lines ->
            lines.associate { parseLine(it) }
        }
    }

    operator fun get(id: Int) = table.getValue(id)
}
