package robott.dy.utils

/**
 * Data format transfer
 * Created by linpeng on 2016/3/30.
 */
object FormatTransfer {

    fun toLH(n: Int): ByteArray? {
        val b = ByteArray(4)
        b[0] = (n and 0xff).toByte()
        b[1] = (n shr 8 and 0xff).toByte()
        b[2] = (n shr 16 and 0xff).toByte()
        b[3] = (n shr 24 and 0xff).toByte()
        return b
    }

    fun toHH(n: Int): ByteArray? {
        val b = ByteArray(4)
        b[3] = (n and 0xff).toByte()
        b[2] = (n shr 8 and 0xff).toByte()
        b[1] = (n shr 16 and 0xff).toByte()
        b[0] = (n shr 24 and 0xff).toByte()
        return b
    }

    fun toLH(n: Short): ByteArray {
        val b = ByteArray(2)
        b[0] = (n.toInt() and 0xff).toByte()
        b[1] = (n.toInt() shr 8 and 0xff).toByte()
        return b
    }

    fun toHH(n: Short): ByteArray {
        val b = ByteArray(2)
        b[1] = (n.toInt() and 0xff).toByte()
        b[0] = (n.toInt() shr 8 and 0xff).toByte()
        return b
    }


}