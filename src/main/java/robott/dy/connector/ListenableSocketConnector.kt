package robott.dy.connector

import robott.dy.listener.SocketMessageListener

/**
 * a socket connector supported customer-pattern
 * Created by linpeng on 2016/3/31.
 */
interface ListenableSocketConnector : SocketConnector {
    /**
     * registe listener
     */
    fun registe(vararg listener: SocketMessageListener)

    /**
     * broadcast message to listeners
     */
    fun broadcast(data: ByteArray)
}