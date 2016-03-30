package robott.dy.utils

import org.apache.commons.lang3.StringUtils
import robott.dy.model.DouyuEncoder
import java.io.ByteArrayOutputStream
import java.io.DataOutputStream

/**
 * Message object,translator of douyu socket message protocol
 * Created by linpeng on 2016/3/30.
 */
object DouyuMessage {
    val DY_MESSAGE_TYPE_CLIENT = 689;

    /**
     * translate to login request data
     */
    fun getLoginRequestData(roomId: Int): ByteArray? {
        //编码器初始化
        val encoder = DouyuEncoder()
        //添加登录协议type类型,添加登录房间ID
        encoder.addItem("type", "loginreq").addItem("roomid", roomId)
        //返回登录协议数据
        return getByte(encoder.getResult())
        //  return encoder.getResult().toByteArray();
    }

    /**
     * str to byte
     */
    private fun getByte(data: String): ByteArray? {
        val boutput = ByteArrayOutputStream()
        val doutput = DataOutputStream(boutput)

        boutput.reset()
        doutput.write(FormatTransfer.toLH(data.length + 8), 0, 4)        // 4 bytes packet length
        doutput.write(FormatTransfer.toLH(data.length + 8), 0, 4)        // 4 bytes packet length
        doutput.write(FormatTransfer.toLH(DY_MESSAGE_TYPE_CLIENT), 0, 2)   // 2 bytes message type
        doutput.writeByte(0)                                               // 1 bytes encrypt
        doutput.writeByte(0)                                               // 1 bytes reserve
        doutput.writeBytes(data)

        return boutput.toByteArray()
    }

    /**
     * translate douyu login response data
     */
    fun parseLoginRespond(recvByte: ByteArray): Boolean {
        var rtn = false

        //返回数据不正确（仅包含12位信息头，没有信息内容）
        if (recvByte.size <= 12) {
            return rtn
        }

        //解析返回信息包中的信息内容
        val dataStr = String(recvByte, 12, recvByte.size - 12)

        //针对登录返回信息进行判断
        if (StringUtils.contains(dataStr, "type@=loginres")) {
            rtn = true
        }

        //返回登录是否成功判断结果
        return rtn
    }

    /**
     * translate to join group request data
     */
    fun getJoinGroupRequest(roomId: Int, groupId: Int): ByteArray? {
        val encoder = DouyuEncoder()
        encoder.addItem("type", "joingroup").addItem("rid", roomId).addItem("gid", groupId)
        return getByte(encoder.getResult())
    }

    /**
     * translate to keep alive request data
     */
    fun getKeepAliveData(time: Long): ByteArray? {
        //编码器初始化
        val enc = DouyuEncoder()
        //添加心跳协议type类型
        enc.addItem("type", "keeplive").addItem("tick", time);
        //返回心跳协议数据
        return getByte(enc.getResult());
    }
}