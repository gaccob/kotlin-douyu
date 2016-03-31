package robott.dy.listener

import robott.dy.event.SocketEvent

/**
 * Heartbeat event handler
 * Created by linpeng on 2016/3/31.
 */
object HeartbeatEventPrinter : SocketMessageListener {
    override fun onMessage(data: ByteArray) {
        throw UnsupportedOperationException()
    }

    override fun onMessage(event: SocketEvent) {
        throw UnsupportedOperationException()
    }
}