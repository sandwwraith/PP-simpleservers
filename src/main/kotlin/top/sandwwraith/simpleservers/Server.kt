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

class Server(port: Int = 8080, private val clock: LamportClock) {
    private val httpServer = AppServer(AppConfiguration(port = port))

    private val Log = LoggerFactory.getLogger(this.javaClass)

    private val responder = routeHandler {
        val from = request.queryParams["id"]?.toIntOrNull()
        val msg = request.queryParams["msg"]
        val time = request.queryParams["time"]?.toIntOrNull()
        if (from != null && msg != null && time != null) {
            Log.info("received from:$from msg:${URLDecoder.decode(msg, "UTF-8")} time:${clock.updateTime(time)}")
            response.statusCode = 200
        } else {
            Log.error("Necessary parameters were not specified or incorrect!")
            response.statusCode = 400
        }
    }

    init {
        httpServer.get("/", responder)
    }

    fun start(wait: Boolean = false) = httpServer.start(wait)

    fun stop() = httpServer.stop()
}
