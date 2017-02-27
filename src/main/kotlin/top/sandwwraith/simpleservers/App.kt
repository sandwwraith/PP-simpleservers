package top.sandwwraith.simpleservers

import org.slf4j.LoggerFactory


/**
 * @author Leonid Startsev
 *		  sandwwraith@gmail.com
 * 		  ITMO University, 2017
 **/

fun main(args: Array<String>) {
    val l = LoggerFactory.getLogger("top.sandwwraith.App")

    val server = Server().apply { start(wait = false) }
    l.info("Type msg <message> to send message")
    l.info("Hit ^D to stop server")

    val client = Client()

    loop@ while (true) {
        val com = readLine()
        when {
            com.isNullOrEmpty() -> {
                l.info("Shutting down")
                server.stop()
                break@loop
            }
            com!!.startsWith("msg ") -> {
                val msg = com.substring(4)
                if (msg.isNotBlank()) client.send(msg)
            }
            else -> {
                l.warn("Unknown command")
            }
        }
    }
}
