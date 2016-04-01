package robott.dy.protocol.clause

import robott.dy.utils.FormatTransfer
import java.io.ByteArrayOutputStream
import java.io.DataOutputStream

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

    public fun convert(buffer: StringBuffer, Entry: Map.Entry<String, Any>) {
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

}