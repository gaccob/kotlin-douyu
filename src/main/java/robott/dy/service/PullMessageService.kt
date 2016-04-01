package robott.dy.service

/**
 * Pull message thread
 * Created by linpeng on 2016/4/1.
 */
object PullMessageService : Thread() {
    override fun run() {

        while (DouyuService.getReadyFlag()) {
            DouyuService.pull();
        }
    }
}