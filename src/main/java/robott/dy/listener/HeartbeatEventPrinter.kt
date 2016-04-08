package robott.dy.listener

import org.apache.log4j.Logger
import robott.dy.event.SocketEvent
import robott.dy.event.SocketEventType

/**
 * Heartbeat event handler
 * Created by linpeng on 2016/3/31.
 */
object HeartbeatEventPrinter : SocketMessageListener {
    internal val logger = Logger.getLogger(this.javaClass)

    override fun listenerFor(): SocketEventType = SocketEventType.HEARTBEAT

    override fun onMessage(data: ByteArray) {
        throw UnsupportedOperationException()
    }

    override fun onMessage(event: SocketEvent) {
        logger.debug("[心跳] %s".format(event.toText()))
    }
}