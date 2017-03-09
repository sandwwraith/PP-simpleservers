package top.sandwwraith.simpleservers

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.system.exitProcess


/**
 * @author Leonid Startsev
 *		  sandwwraith@gmail.com
 * 		  ITMO University, 2017
 **/

private inline fun <T> runOrFatal(log: Logger, block: () -> T): T {
    try {
        return block()
    } catch (e: Exception) {
        log.error(e.message, e)
        exitProcess(-1)
    }
}

fun main(args: Array<String>) {
    val log = LoggerFactory.getLogger("top.sandwwraith.App")

    val myId = runOrFatal(log) { args[0].toInt() }
    val router = runOrFatal(log) { Router() }

    val server = Server(port = router[myId].second).apply { start(wait = false) }
    log.info("Type msg <message> to send message")
    log.info("Hit ^D to stop server")

    val client = Client()

    loop@ while (true) {
        val com = readLine()
        when {
            com.isNullOrEmpty() -> {
                log.info("Shutting down")
                server.stop()
                break@loop
            }
            com!!.startsWith("msg ") -> {
                val msg = com.substring(4)
                if (msg.isNotBlank()) client.send(msg)
            }
            else -> {
                log.warn("Unknown command")
            }
        }
    }
}
