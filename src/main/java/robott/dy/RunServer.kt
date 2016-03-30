package robott.dy

import robott.dy.model.DouyuBulletScreenHolder
import robott.dy.model.FetchMessageThread
import robott.dy.model.KeepAliveThread

/**
 * Kotlin main function
 * Created by linpeng on 2016/3/30.
 */

private val roomId = 56040

//弹幕池分组号，海量模式使用-9999
private val groupId = -9999

fun main(args: Array<String>) {
    // socket connect and data prepare
    DouyuBulletScreenHolder.initialize(roomId, groupId)
    // keep alive
    KeepAliveThread().start()
    // fetch data  by bullet screen holder object
    FetchMessageThread().start()

}
