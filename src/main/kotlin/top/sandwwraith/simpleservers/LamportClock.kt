package top.sandwwraith.simpleservers

import java.util.concurrent.atomic.AtomicInteger

/**
 * @author Leonid Startsev
 *		  sandwwraith@gmail.com
 * 		  ITMO University, 2017
 **/
class LamportClock {
    private val clock = AtomicInteger(0)

    val sendTime get() = clock.incrementAndGet()

    fun updateTime(recvTime: Int) = clock.accumulateAndGet(recvTime, { cur, upd -> maxOf(cur, upd) + 1 })
}
