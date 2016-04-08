package robott.dy.service

import com.github.kittinunf.fuel.httpGet
import org.apache.log4j.Logger
import robott.dy.connector.DouyuSocketConnector
import robott.dy.data.LiveRoom
import robott.dy.data.LiveRoomDataResp
import robott.dy.event.SocketEventType
import robott.dy.listener.BulletScreenEventPrinter
import robott.dy.listener.ConnectEventPrinter
import robott.dy.listener.HeartbeatEventPrinter
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
class DouyuService {
    internal val logger = Logger.getLogger(this.javaClass)

    private val liveRoomUrl = "http://capi.douyucdn.cn/api/v1/live?limit=%d&offset=%d"
    private var connector = DouyuSocketConnector(this)
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
     * pull operation
     */
    fun pull() {
        //        throw UnsupportedOperationException("not implemented")
        connector.pull(protocol);
    }

    /**
     * registe listener
     */
    fun registe(eventType: SocketEventType, listener: SocketMessageListener) {
        throw UnsupportedOperationException("not implemented")
    }

    fun getReadyFlag(): Boolean = readyFlag

    fun setReadFlag(readFlag: Boolean) {
        this.readyFlag = readFlag
    }

    private fun prepare() {
        protocol.addClause(ConnectClauseObject, HeartbeatClauseObject, BulletScreenClauseObject, JoinGroupClauseObject)
        connector.registe(ConnectEventPrinter, HeartbeatEventPrinter, BulletScreenEventPrinter)
    }

    /**
     * Get live room id by BLOCKING-MODE
     * maxCount <= 0 mean get all rooms
     */
    fun liveRooms(maxCount: Int): List<LiveRoom> {
        var rooms = mutableListOf<LiveRoom>()
        var offset = 0
        var limit = 100
        // this should be while-statement
        var isContinue = true

        try {
            while (isContinue) {
                // Blocking mode
                val (request, response, result) = liveRoomUrl.format(limit, offset).httpGet().responseObject(LiveRoomDataResp.Deserializer())
                val resp = result.get()

                if (resp.error.contentEquals("0")) {
                    // add liverooms to rooms
                    rooms.addAll(resp.data)

                    if (logger.isDebugEnabled) {
                        logger.debug("Get live room %d to %d success!".format(offset, offset + limit))
                        //  logger.debug("[RESP] %s".format(resp))
                    }

                    // the new begin index
                    offset = offset + limit

                    // check is rooms enough
                    if (rooms.size >= maxCount && maxCount > 0 ) {
                        isContinue = false
                    }
                } else {
                    isContinue = false
                }
            }
        } catch(e: Exception) {
            logger.error("Get live rooms error !", e)
        }
        return rooms
    }


}