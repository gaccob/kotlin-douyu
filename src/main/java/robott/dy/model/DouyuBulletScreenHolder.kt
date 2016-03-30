package robott.dy.model

import org.apache.commons.lang3.StringUtils
import org.apache.log4j.Logger
import robott.dy.utils.DouyuMessage
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.net.InetAddress
import java.net.Socket

/**
 * douyu socket connection holder
 * Created by linpeng on 2016/3/30.
 */
object DouyuBulletScreenHolder {

    internal val logger = Logger.getLogger(this.javaClass)

    //第三方弹幕协议服务器地址
    private val hostName = "openbarrage.douyutv.com"
    //第三方弹幕协议服务器端口
    private val port = 8601
    //设置字节获取buffer的最大值
    private val MAX_BUFFER_LENGTH = 4096
    //socket相关配置
    private var sock: Socket? = null
    private var bos: BufferedOutputStream? = null
    private var bis: BufferedInputStream? = null


    //获取弹幕线程及心跳线程运行和停止标记
    private var readyFlag = false

    /**
     * test if connection get ready
     */
    fun getReadyFlag(): Boolean = readyFlag

    /**
     * get latest bullet screen data by socket
     */
    fun getServerMsg() {
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
        //根据TCP协议获取返回信息中的字符串信息
        dataStr = String(realBuf, 12, realBuf.size - 12)
        //循环处理socekt黏包情况
        while (dataStr.lastIndexOf("type@=") > 5) {
            //对黏包中最后一个数据包进行解析
            val msgView = DouyuMessageView(StringUtils.substring(dataStr, dataStr.lastIndexOf("type@=")))
            //分析该包的数据类型，以及根据需要进行业务操作
            parseServerMsg(msgView.messageList)
            //处理黏包中的剩余部分
            dataStr = StringUtils.substring(dataStr, 0, dataStr.lastIndexOf("type@=") - 12)
        }

        //对单一数据包进行解析
        val msgView = DouyuMessageView(StringUtils.substring(dataStr, dataStr.lastIndexOf("type@=")))
        //分析该包的数据类型，以及根据需要进行业务操作
        parseServerMsg(msgView.messageList)
    }

    /**
     * parse bullet screen data
     */
    private fun parseServerMsg(msg: Map<String, Any>) {
        if (msg.get("type") != null) {

            //服务器反馈错误信息
            if (msg.get("type").toString().contentEquals("error")) {
                logger.debug(msg.toString())
                //结束心跳和获取弹幕线程
                this.readyFlag = false
            }

            //判断消息类型
            if (msg.get("type").toString().contentEquals("chatmsg")) {
                //弹幕消息
                logger.debug("弹幕消息===>" + msg.toString())
            } else if (msg.get("type").toString().contentEquals("dgb")) {
                //赠送礼物信息
                logger.debug("礼物消息===>" + msg.toString())
            } else {
                logger.debug("其他消息===>" + msg.toString())
            }


        }
    }

    /**
     * init function
     */
    fun initialize(roomId: Int, groupId: Int) {
        //连接弹幕服务器
        connectServer()
        //登陆指定房间
        loginRoom(roomId)
        //加入指定的弹幕池
        joinGroup(roomId, groupId)
        //设置客户端就绪标记为就绪状态
        readyFlag = true
    }

    /**
     * join group operation
     */
    private fun joinGroup(roomId: Int, groupId: Int) {
        //获取弹幕服务器加弹幕池请求数据包
        val joinGroupRequest = DouyuMessage.getJoinGroupRequest(roomId, groupId)

        try {
            //想弹幕服务器发送加入弹幕池请求数据
            bos?.write(joinGroupRequest, 0, joinGroupRequest!!.size)
            bos?.flush()
            logger.debug("Send join group request successfully!")
        } catch(e: Exception) {
            logger.error("Send join group request failed!", e)
        }
    }

    /**
     * login room operation
     */
    private fun loginRoom(roomId: Int) {
        val loginRequestData: ByteArray? = DouyuMessage.getLoginRequestData(roomId)
        //发送登陆请求数据包给弹幕服务器
        bos?.write(loginRequestData, 0, loginRequestData!!.size)
        bos?.flush()

        //初始化弹幕服务器返回值读取包大小
        val recvByte = ByteArray(MAX_BUFFER_LENGTH)
        //获取弹幕服务器返回值
        bis?.read(recvByte, 0, recvByte.size)

        //解析服务器返回的登录信息
        if (DouyuMessage.parseLoginRespond(recvByte)) {
            logger.debug("Receive login response successfully!")
        } else {
            logger.error("Receive login response failed!")
        }
    }

    /**
     * connect bullet screen server operation
     */
    private fun connectServer() {
        //获取弹幕服务器访问host
        val host = InetAddress.getByName(hostName).hostAddress
        //建立socke连接
        sock = Socket(host, port)
        //设置socket输入及输出
        bos = BufferedOutputStream((sock as Socket).getOutputStream())
        bis = BufferedInputStream((sock as Socket).getInputStream())

        logger.debug("Server Connect Successfully!")
    }

    /**
     * Keep socket connection alive
     */
    fun keepAlive() {
        //获取与弹幕服务器保持心跳的请求数据包
        val keepAliveRequest: ByteArray? = DouyuMessage.getKeepAliveData((System.currentTimeMillis() / 1000));
        try {
            //向弹幕服务器发送心跳请求数据包
            bos?.write(keepAliveRequest, 0, keepAliveRequest?.size!!);
            bos?.flush();
            logger.debug("Send keep alive request successfully!");
        } catch(e: Exception) {
            logger.error("Send keep alive request failed!", e);
        }

    }
}