package robott.dy.connector

import org.apache.commons.lang3.StringUtils
import org.apache.log4j.Logger
import robott.dy.event.BulletScreenEvent
import robott.dy.event.HeartbeatEvent
import robott.dy.event.SocketEvent
import robott.dy.event.SocketEventType
import robott.dy.listener.SocketMessageListener
import robott.dy.protocol.Protocol
import robott.dy.protocol.clause.DouyuEncoderObject
import robott.dy.service.DouyuService
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.net.InetAddress
import java.net.Socket

/**
 * Socket connector for douyu
 * Created by linpeng on 2016/3/31.
 */
class DouyuSocketConnector(douyuService: DouyuService) : ListenableSocketConnector {
    internal val logger = Logger.getLogger(this.javaClass)

    private val douyuService = douyuService
    private val hostName = "openbarrage.douyutv.com"
    //第三方弹幕协议服务器端口
    private val port = 8601
    //设置字节获取buffer的最大值
    private val MAX_BUFFER_LENGTH = 4096

    private var sock: Socket? = null
    private var bos: BufferedOutputStream? = null
    private var bis: BufferedInputStream? = null

    private var listeners: MutableList<SocketMessageListener> = mutableListOf()

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

    override fun registe(vararg listener: SocketMessageListener) {
        listeners.addAll(listener)
        if (logger.isDebugEnabled) {
            logger.debug("add listener %s !".format(listener))
        }
    }

    override fun broadcast(data: ByteArray) {
        //根据TCP协议获取返回信息中的字符串信息
        var dataStr = String(data, 12, data.size - 12)
        while (dataStr.lastIndexOf("type@=") > 5) {
            //分析该包的数据类型，以及根据需要进行业务操作
            broadcastInternal(dataStr)
            //处理黏包中的剩余部分
            dataStr = StringUtils.substring(dataStr, 0, dataStr.lastIndexOf("type@=") - 12)
        }
        broadcastInternal(dataStr)
    }

    private fun broadcastInternal(dataStr: String) {
        //对黏包中最后一个数据包进行解析
        val tempMsg = StringUtils.substring(dataStr, dataStr.lastIndexOf("type@="))
        val map: Map<String, Any> = DouyuEncoderObject.parsetToMap(tempMsg);
        if (map.get("type") != null) {
            //服务器反馈错误信息
            if (map.get("type").toString().contentEquals("error")) {
                logger.debug(map.toString())
                //结束心跳和获取弹幕线程
                douyuService.setReadFlag(false)
            }

            val emptyByteArray = "".toByteArray()
            var event: SocketEvent? = null
            //判断消息类型
            when (map.get("type").toString()) {
                "chatmsg" -> {
                    event = BulletScreenEvent(emptyByteArray, map.toString())
                }
                "keeplive" -> {
                    event = HeartbeatEvent(emptyByteArray, map.toString())
                }
                else -> {
                    logger.debug("[Unknow] -> " + map.toString())
                }
            }
            if (event != null) {
                broadcast(event)
            }

        }
    }


    override fun heartbeat(data: ByteArray) {
        try {
            //向弹幕服务器发送心跳请求数据包
            bos?.write(data, 0, data.size);
            bos?.flush();
            logger.debug("Send keep alive request successfully!");
        } catch(e: Exception) {
            logger.error("Send keep alive request failed!", e);
        }
    }

    fun heartbeat(protocol: Protocol) {
        heartbeat(protocol.getClause(SocketEventType.HEARTBEAT).get(0).toServer(mapOf(Pair("type", "keeplive"), Pair("tick", (System.currentTimeMillis() / 1000)))))
    }

    override fun pull(data: ByteArray) {
        //初始化获取弹幕服务器返回信息包大小
        val recvByte = ByteArray(MAX_BUFFER_LENGTH)
        //定义服务器返回信息的字符串
        var dataStr: String

        //读取服务器返回信息，并获取返回信息的整体字节长度
        val recvLen = bis?.read(recvByte, 0, recvByte.size)
        //根据实际获取的字节数初始化返回信息内容长度
        val realBuf = ByteArray(recvLen as Int)
        //按照实际获取的字节长度读取返回信息
        System.arraycopy(recvByte, 0, realBuf, 0, recvLen)
        // broadcast receive bytes
        broadcast(realBuf)
    }

    fun pull(protocol: Protocol) {
        pull(protocol.getClause(SocketEventType.BULLETSCREEN).get(0).toServer(mapOf()))
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
        when (event) {
            is HeartbeatEvent -> {
                listeners.filter({ it.listenerFor().equals(SocketEventType.HEARTBEAT) }).forEach { it.onMessage(event) }
            }
            is BulletScreenEvent -> {
                listeners.filter({ it.listenerFor().equals(SocketEventType.BULLETSCREEN) }).forEach { it.onMessage(event) }
            }
            else -> {
                if (logger.isDebugEnabled) {
                    logger.debug("Unbroadcast or unsupported event %s".format(event))
                }
            }
        }
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