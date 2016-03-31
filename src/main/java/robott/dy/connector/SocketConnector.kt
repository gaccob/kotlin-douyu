package robott.dy.connector

/**
 * Socket connector in low level
 * Created by linpeng on 2016/3/31.
 */
interface SocketConnector {
    /**
     * Connect to remote server socket
     */
    fun connect(data: ByteArray): ByteArray

    /**
     * Disconnect from remote server socket
     */
    fun disconnect(data: ByteArray)

    /**
     * Keep socket connection alive
     */
    fun heartbeat(data: ByteArray)

    /**
     * Pull data from remote server socket
     */
    fun pull(data: ByteArray)
}