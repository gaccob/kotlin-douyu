package robott.dy.protocol

import robott.dy.event.SocketEventType
import robott.dy.protocol.clause.Clause

/**
 * Socket data protocol
 * Created by linpeng on 2016/3/31.
 */
interface Protocol {
    /**
     * Add a clause to protocol
     */
    fun addClause(clause: Clause): Protocol

    /**
     * get clause by socket event type
     */
    fun getClause(type: SocketEventType): List<Clause>
}