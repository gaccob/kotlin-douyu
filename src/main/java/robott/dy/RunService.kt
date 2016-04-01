package robott.dy

import robott.dy.service.DouyuService
import robott.dy.service.HeartbeatService
import robott.dy.service.PullMessageService

/**
 * Run service script
 * Created by linpeng on 2016/3/31.
 */
private val roomId = 56040 //138286

//弹幕池分组号，海量模式使用-9999
private val groupId = -9999

fun main(args: Array<String>) {
    // socket connect and data prepare
    DouyuService.connect(roomId, groupId)
    // keep alive
    HeartbeatService.start()
    // pull message
    PullMessageService.start()
}