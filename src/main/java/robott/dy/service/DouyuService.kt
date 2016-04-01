package robott.dy.service

import org.apache.log4j.Logger
import robott.dy.connector.DouyuSocketConnector
import robott.dy.event.SocketEventType
import robott.dy.listener.SocketMessageListener
import robott.dy.protocol.DouyuSocketProtocol
import robott.dy.protocol.clause.BulletScreenClauseObject
import robott.dy.protocol.clause.ConnectClauseObject
import robott.dy.protocol.clause.HeartbeatClauseObject
import robott.dy.protocol.clause.JoinGroupClauseObject

/**
 * Douyu service object
 * Created by linpeng on 2016/3/31.
 */
object DouyuService {
    internal val logger = Logger.getLogger(this.javaClass)

    private var connector = DouyuSocketConnector
    private var protocol = DouyuSocketProtocol
    private var readyFlag = false

    init {
        prepare()
    }

    /**
     * Connect to server
     */
    fun connect(rid: Int, gid: Int) {
        readyFlag = connector.connect(rid, gid, protocol)
    }

    /**
     * heartbeat
     */
    fun heartbeat() = connector.heartbeat(protocol)

    /**
     * registe listener
     */
    fun registe(eventType: SocketEventType, listener: SocketMessageListener) {

    }

    fun getReadyFlag(): Boolean = readyFlag

    private fun prepare() {
        protocol.addClause(ConnectClauseObject).addClause(HeartbeatClauseObject).addClause(BulletScreenClauseObject).addClause(JoinGroupClauseObject)
    }

}