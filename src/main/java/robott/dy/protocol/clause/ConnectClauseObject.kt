package robott.dy.protocol.clause

import robott.dy.event.ConnectEvent
import robott.dy.event.SocketEvent
import robott.dy.event.SocketEventType

/**
 * Connect Clause
 * Created by linpeng on 2016/3/31.
 */
object ConnectClauseObject : Clause {
    override fun clauseFor(): SocketEventType = SocketEventType.CONNECT

    override fun toClient(byteArray: ByteArray): SocketEvent {
        return ConnectEvent(byteArray, "connected")
    }

    override fun toServer(map: Map<String, Any>): ByteArray = DouyuEncoderObject.asBytes(map)
}