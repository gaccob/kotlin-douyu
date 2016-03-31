package robott.dy.connector

import org.apache.commons.lang3.StringUtils
import org.apache.log4j.Logger
import robott.dy.event.SocketEvent
import robott.dy.event.SocketEventType
import robott.dy.listener.SocketMessageListener
import robott.dy.protocol.Protocol
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.net.InetAddress
import java.net.Socket

/**
 * Socket connector for douyu
 * Created by linpeng on 2016/3/31.
 */
object DouyuSocketConnector : ListenableSocketConnector {
    internal val logger = Logger.getLogger(this.javaClass)

    private val hostName = "openbarrage.douyutv.com"
    //第三方弹幕协议服务器端口
    private val port = 8601
    //设置字节获取buffer的最大值
    private val MAX_BUFFER_LENGTH = 4096

    private var sock: Socket? = null
    private var bos: BufferedOutputStream? = null
    private var bis: BufferedInputStream? = null

    override fun connect(data: ByteArray): ByteArray {
        //建立socke连接
        sock = Socket(InetAddress.getByName(hostName).hostAddress, port)
        //设置socket输入及输出
        bos = BufferedOutputStream((sock as Socket).getOutputStream())
        bis = BufferedInputStream((sock as Socket).getInputStream())



        //发送登陆请求数据包给弹幕服务器
        bos?.write(data, 0, data.size)
        bos?.flush()

        val recvByte = ByteArray(MAX_BUFFER_LENGTH)
        //获取弹幕服务器返回值
        bis?.read(recvByte, 0, recvByte.size)

        logger.debug("Server Connect Successfully!")

        return recvByte
    }

    override fun disconnect(data: ByteArray) {
        throw UnsupportedOperationException()
    }

    override fun registe(listener: SocketMessageListener) {
        throw UnsupportedOperationException()
    }

    override fun broadcast(data: ByteArray) {
        throw UnsupportedOperationException()
    }


    override fun heartbeat(data: ByteArray) {
        throw UnsupportedOperationException()
    }

    override fun pull(data: ByteArray) {
        throw UnsupportedOperationException()
    }


    /**
     * registe listener for douyu
     */
    fun registe(type: SocketEventType, listener: SocketMessageListener) {
    }

    /**
     * broadcast event message to listeners
     */
    fun broadcast(event: SocketEvent) {
    }

    fun connect(rid: Int, gid: Int, protocol: Protocol): Boolean {
        val connectClause = protocol.getClause(SocketEventType.CONNECT).get(0)
        val loginRequestData = connectClause.toServer(mapOf(Pair("type", "loginreq"), Pair("roomid", rid)))
        val recvByte = connect(loginRequestData)

        //解析服务器返回的登录信息
        if (check(connectClause.toClient(recvByte))) {
            logger.debug("Receive login response successfully!")
            // Join group
            joinGroup(rid, gid, protocol)
            return true
        } else {
            logger.error("Receive login response failed!")
        }
        return false
    }

    private fun check(event: SocketEvent): Boolean {
        var rtn = false

        //返回数据不正确（仅包含12位信息头，没有信息内容）
        if (event.getRaw().size <= 12) {
            return rtn
        }

        //解析返回信息包中的信息内容
        val dataStr = String(event.getRaw(), 12, event.getRaw().size - 12)

        //针对登录返回信息进行判断
        if (StringUtils.contains(dataStr, "type@=loginres")) {
            rtn = true
        }

        //返回登录是否成功判断结果
        return rtn
    }

    private fun joinGroup(rid: Int, gid: Int, protocol: Protocol) {
        val map = mapOf(Pair("type", "joingroup"), Pair("rid", rid), Pair("gid", gid))
        val joinGroupRequestData = protocol.getClause(SocketEventType.JOINGROUP).get(0).toServer(map)

        try {
            //想弹幕服务器发送加入弹幕池请求数据
            bos?.write(joinGroupRequestData, 0, joinGroupRequestData.size)
            bos?.flush()
            logger.debug("Send join group request successfully!")
        } catch(e: Exception) {
            logger.error("Send join group request failed!", e)
        }
    }
}