package robott.dy.listener

import robott.dy.event.SocketEvent
import robott.dy.event.SocketEventType

/**
 * Socket message listener
 * Created by linpeng on 2016/3/31.
 */
interface SocketMessageListener {
    /**
     * Do when binary message come
     */
    fun onMessage(data: ByteArray)

    /**
     * Do when socket message come
     */
    fun onMessage(event: SocketEvent)

    /**
     * Tell what kind socket event it listen
     */
    fun listenerFor(): SocketEventType;
}