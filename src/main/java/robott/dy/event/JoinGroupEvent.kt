package robott.dy.event

/**
 * Created by linpeng on 2016/4/1.
 */
class JoinGroupEvent(byteArray: ByteArray, text: String) : SocketEvent {
    val byteArray = byteArray
    val text = text

    override fun getRaw(): ByteArray = byteArray

    override fun toText() = text

    override fun getEventType(): SocketEventType = SocketEventType.JOINGROUP


}