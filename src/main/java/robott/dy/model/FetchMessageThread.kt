package robott.dy.model

/**
 * Thread of fetch data
 * Created by linpeng on 2016/3/30.
 */
class FetchMessageThread : Thread() {

    override fun run() {
        while (DouyuBulletScreenHolder.getReadyFlag()) {
            DouyuBulletScreenHolder.getServerMsg();
        }
    }
}