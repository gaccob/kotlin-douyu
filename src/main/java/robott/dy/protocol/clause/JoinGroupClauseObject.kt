package robott.dy.protocol.clause

import robott.dy.event.JoinGroupEvent
import robott.dy.event.SocketEvent
import robott.dy.event.SocketEventType

/**
 * Created by linpeng on 2016/4/1.
 */
object JoinGroupClauseObject : Clause {
    override fun toClient(byteArray: ByteArray): SocketEvent {
        return JoinGroupEvent(byteArray, "heartbeat")
    }

    override fun toServer(map: Map<String, Any>): ByteArray = DouyuEncoderObject.asBytes(map)

    override fun clauseFor(): SocketEventType = SocketEventType.JOINGROUP
}