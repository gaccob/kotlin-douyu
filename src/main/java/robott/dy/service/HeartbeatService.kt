package robott.dy.service

import org.apache.log4j.Logger

/**
 * Heartbeat service
 * Created by linpeng on 2016/3/31.
 */
object HeartbeatService : Thread() {

    internal val logger = Logger.getLogger(this.javaClass)

    override fun run() {
        while (DouyuService.getReadyFlag()) {
            DouyuService.heartbeat()
            logger.debug("- -! heartbeat")
            try {
                //设置间隔45秒再发送心跳协议
                Thread.sleep(45 * 1000);        //keep live at least once per minute
            } catch (e: Exception) {
                logger.error("HeartbeatService error", e)
            }
        }

    }
}