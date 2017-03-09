package top.sandwwraith.simpleservers

import org.slf4j.LoggerFactory
import java.util.regex.Pattern
import kotlin.system.exitProcess


/**
 * @author Leonid Startsev
 *		  sandwwraith@gmail.com
 * 		  ITMO University, 2017
 **/

private inline fun <T> runOrFatal(block: () -> T, loggingAction: (Exception) -> Any?): T {
    try {
        return block()
    } catch (e: Exception) {
        loggingAction(e)
        exitProcess(-1)
    }
}

private val sendPattern = Pattern.compile("""^send to:(\d+) msg:(.+)$""")
private fun parseSendCommand(line: String): Pair<Int, String>? {
    val m = sendPattern.matcher(line)
    if (!m.matches()) return null
    val id = m.group(1).toIntOrNull() ?: return null
    val msg = m.group(2)
    return id to msg
}

fun main(args: Array<String>) {
    val log = LoggerFactory.getLogger("top.sandwwraith.App")

    val myId = runOrFatal({ args[0].toInt() }, { log.error("ID of this process should be specified as a command-line argument", it) })
    val router = runOrFatal({ Router() }, { log.error("Error reading config file", it) })
    val port = runOrFatal({ router[myId].second }, { log.error("Can't find port in config file for this ID", it) })
    val clock = LamportClock()

    log.info("Starting server... My id: $myId, my port: $port")
    val server = Server(port, clock).apply { start(wait = false) }
    log.info("Started. Hit ^D to stop server")

    val client = Client()

    loop@ while (true) {
        val com = readLine()
        when {
            com.isNullOrEmpty() -> {
                log.info("Shutting down")
                server.stop()
                break@loop
            }
            com!!.startsWith("send") -> {
                parseSendCommand(com)?.let { (id, msg) ->
                    client.send(myId, msg, clock.sendTime, router[id].first, router[id].second)
                } ?: log.warn("Send command is incorrect")
            }
            else -> log.warn("Unknown command")
        }
    }
}
