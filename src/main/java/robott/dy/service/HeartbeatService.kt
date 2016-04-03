package robott.dy.service

import org.apache.log4j.Logger

/**
 * Heartbeat service
 * Created by linpeng on 2016/3/31.
 */
class HeartbeatService(douyuService: robott.dy.service.DouyuService) : Thread() {

    internal val logger = Logger.getLogger(this.javaClass)
    val douyuService = douyuService

    override fun run() {
        while (douyuService.getReadyFlag()) {
            douyuService.heartbeat()
            logger.debug("heartbeat called")
            try {
                //设置间隔45秒再发送心跳协议
                Thread.sleep(45 * 1000);        //keep live at least once per minute
            } catch (e: Exception) {
                logger.error("HeartbeatService error", e)
            }
        }

    }
}