package robott.dy.service

/**
 * Pull message thread
 * Created by linpeng on 2016/4/1.
 */
class PullMessageService(douyuService: robott.dy.service.DouyuService) : Thread() {
    val douyuService = douyuService
    override fun run() {
        while (douyuService.getReadyFlag()) {
            douyuService.pull();
        }
    }
}