package robott.dy.protocol.clause

import robott.dy.event.HeartbeatEvent
import robott.dy.event.SocketEvent
import robott.dy.event.SocketEventType

/**
 * Heartbeat clause
 * Created by linpeng on 2016/3/31.
 */
object HeartbeatClauseObject : Clause {
    override fun clauseFor(): SocketEventType = SocketEventType.HEARTBEAT

    override fun toClient(byteArray: ByteArray): SocketEvent {
        return HeartbeatEvent(byteArray, "heartbeat")
    }

    override fun toServer(map: Map<String, Any>): ByteArray = DouyuEncoderObject.asBytes(map)
}