package robott.dy.model

import org.apache.log4j.Logger

/**
 * Keep socket connection alive
 * Created by linpeng on 2016/3/30.
 */
class KeepAliveThread : Thread() {
    internal val logger = Logger.getLogger(this.javaClass)

    override fun run() {
        //判断客户端就绪状态
        while (DouyuBulletScreenHolder.getReadyFlag()) {
            //发送心跳保持协议给服务器端
            DouyuBulletScreenHolder.keepAlive();
            try {
                //设置间隔45秒再发送心跳协议
                Thread.sleep(45 * 1000);        //keep live at least once per minute
            } catch (e: Exception) {
                logger.error("KeepAlive error", e)
            }
        }

    }
}