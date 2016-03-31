package robott.dy.protocol.clause

import robott.dy.event.SocketEvent
import robott.dy.event.SocketEventType

/**
 * Socket data protocol clause
 * Created by linpeng on 2016/3/31.
 */
interface Clause {
    /**
     * translate data for client-side
     */
    fun toClient(byteArray: ByteArray): SocketEvent

    /**
     * translate data for server-side
     */
    fun toServer(map: Map<String, Any>): ByteArray

    /**
     * clause for what
     */
    fun clauseFor(): SocketEventType


}