package robott.dy.listener

import org.apache.log4j.Logger
import robott.dy.event.SocketEvent
import robott.dy.event.SocketEventType

/**
 * Connect event handler
 * Created by linpeng on 2016/3/31.
 */
object ConnectEventPrinter : SocketMessageListener {
    internal val logger = Logger.getLogger(this.javaClass)

    override fun listenerFor(): SocketEventType = SocketEventType.CONNECT

    override fun onMessage(data: ByteArray) {
        throw UnsupportedOperationException()
    }

    override fun onMessage(event: SocketEvent) {
        logger.debug("[连接] %s".format(event.toText()))
    }
}