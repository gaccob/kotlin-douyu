package robott.dy.listener

import org.apache.log4j.Logger
import robott.dy.event.SocketEvent
import robott.dy.event.SocketEventType

/**
 * Bullet Screen Event handler
 * Created by linpeng on 2016/3/31.
 */
object BulletScreenEventPrinter : SocketMessageListener {
    internal val logger = Logger.getLogger(this.javaClass)

    override fun listenerFor(): SocketEventType = SocketEventType.BULLETSCREEN

    override fun onMessage(data: ByteArray) {
        throw UnsupportedOperationException()
    }

    override fun onMessage(event: SocketEvent) {
        logger.info("[弹幕] %s".format(event.toText()))
    }

}
