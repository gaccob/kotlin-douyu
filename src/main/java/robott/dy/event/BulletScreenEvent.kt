package robott.dy.event

/**
 * Bullet Screen Event
 * Created by linpeng on 2016/3/31.
 */
class BulletScreenEvent(byteArray: ByteArray, text: String) : SocketEvent {
    val byteArray = byteArray
    val text = text

    override fun getRaw(): ByteArray = byteArray

    override fun toText() = text

    override fun getEventType(): SocketEventType = SocketEventType.BULLETSCREEN
}