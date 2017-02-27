package top.sandwwraith.simpleservers

import org.slf4j.LoggerFactory
import org.wasabifx.wasabi.app.AppConfiguration
import org.wasabifx.wasabi.app.AppServer
import org.wasabifx.wasabi.routing.routeHandler
import java.net.URLDecoder

/**
 * @author Leonid Startsev
 *		  sandwwraith@gmail.com
 * 		  ITMO University, 2017
 **/

class Server(port: Int = 8080) {
    private val httpServer = AppServer(AppConfiguration(port = port))

    private val Log = LoggerFactory.getLogger(this.javaClass)

    private val responder = routeHandler {
        val msg = request.queryParams["msg"]
        if (msg != null) {
            Log.info("Got message: ${URLDecoder.decode(msg,"UTF-8")}")
            response.statusCode = 200
        } else {
            Log.error("Message parameter not specified")
            response.statusCode = 400
        }
    }

    init {
        httpServer.get("/", responder)
    }

    fun start(wait: Boolean = false) = httpServer.start(wait)

    fun stop() = httpServer.stop()
}
