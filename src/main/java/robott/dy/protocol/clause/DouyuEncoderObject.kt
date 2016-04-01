package robott.dy.protocol.clause

import org.apache.commons.lang3.StringUtils
import robott.dy.utils.FormatTransfer
import java.io.ByteArrayOutputStream
import java.io.DataOutputStream
import java.util.*

/**
 * Socket data encoder object
 * Created by linpeng on 2016/3/31.
 */
object DouyuEncoderObject {
    val DY_MESSAGE_TYPE_CLIENT = 689;


    fun asBytes(map: Map<String, Any>): ByteArray {
        val buf = StringBuffer();

        map.forEach { convert(buf, it) }

        buf?.append(0.toChar()).toString()
        return getByte(buf.toString())
    }

    fun convert(buffer: StringBuffer, Entry: Map.Entry<String, Any>) {
        buffer?.append(Entry.key.replace("/".toRegex(), "@S").replace("@".toRegex(), "@A"))
        buffer?.append("@=")
        if (Entry.value is String) {
            buffer?.append(Entry.value.toString().replace("/".toRegex(), "@S").replace("@".toRegex(), "@A"))
        } else if (Entry.value is Int) {
            buffer?.append((Entry.value as Int))
        }
        buffer?.append("/")
    }

    private fun getByte(data: String): ByteArray {
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

    fun parsetToMap(data: String?): Map<String, Any> {
        val rtnMsg = HashMap<String, Any>()

        //处理数据字符串末尾的'/0字符'
        val tempData = StringUtils.substringBeforeLast(data, "/")

        //对数据字符串进行拆分
        val buff = tempData.split("/".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()

        //分析协议字段中的key和value值
        for (tmp in buff) {
            //获取key值
            val key = StringUtils.substringBefore(tmp, "@=")
            //获取对应的value值
            var value: Any = StringUtils.substringAfter(tmp, "@=")

            //如果value值中包含子序列化值，则进行递归分析
            if (StringUtils.contains(value as String, "@A")) {
                value = value.replace("@S".toRegex(), "/").replace("@A".toRegex(), "@")
                value = parsetToMap(value)
            }
            //将分析后的键值对添加到信息列表中
            rtnMsg.put(key, value)
        }
        return rtnMsg
    }

}