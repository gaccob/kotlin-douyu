package robott.dy.protocol.clause

import robott.dy.event.SocketEvent
import robott.dy.event.SocketEventType

/**
 * Bullet screen clause
 * Created by linpeng on 2016/3/31.
 */
object BulletScreenClauseObject : Clause {
    override fun clauseFor(): SocketEventType = SocketEventType.BULLETSCREEN

    override fun toClient(byteArray: ByteArray): SocketEvent {
        throw UnsupportedOperationException()
    }

    override fun toServer(map: Map<String,Any>): ByteArray {
        throw UnsupportedOperationException()
    }

}