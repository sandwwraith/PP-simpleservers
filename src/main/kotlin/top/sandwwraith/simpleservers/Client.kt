package top.sandwwraith.simpleservers

import org.slf4j.LoggerFactory
import java.net.HttpURLConnection
import java.net.URL

/**
 * @author Leonid Startsev
 *		  sandwwraith@gmail.com
 * 		  ITMO University, 2017
 **/

class Client {
    private val Log = LoggerFactory.getLogger(this.javaClass)

    fun send(msg: String) {
        try {
            val conn = URL("http://localhost:8080?msg=$msg")
                    .openConnection() as HttpURLConnection

            conn.connect()
            if (conn.responseCode != 200) {
                Log.warn("Expected 200 response code, found ${conn.responseCode}")
            }
            conn.disconnect()
        } catch (e: Exception) {
            Log.error("Failed to send message", e)
        }
    }
}
