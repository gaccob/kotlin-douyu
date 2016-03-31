package robott.dy.event

/**
 * SocketEvent interface
 * Created by linpeng on 2016/3/31.
 */
interface SocketEvent {
    /**
     * Socket event message to text
     */
    fun toText(): String;

    /**
     * Return event type which is't
     */
    fun getEventType(): SocketEventType

    fun getRaw(): ByteArray;
}