package robott.dy.event

/**
 * Server connect event
 * Created by linpeng on 2016/3/31.
 */
class ConnectEvent(byteArray: ByteArray, text: String) : SocketEvent {

    val byteArray = byteArray
    val text = text

    override fun getRaw(): ByteArray = byteArray

    override fun toText() = text

    override fun getEventType(): SocketEventType = SocketEventType.CONNECT
}