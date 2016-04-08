package robott.dy

import robott.dy.service.DouyuService
import robott.dy.service.HeartbeatService
import robott.dy.service.PullMessageService

/**
 * Run service script
 * Created by linpeng on 2016/3/31.
 */
//private val roomId = 14163//56040 //138286

//弹幕池分组号，海量模式使用-9999
private val groupId = -9999

fun main(args: Array<String>) {
    // per room per 3 thread running
    val maxRoomCount = 10
    // get live rooms
    val rooms = DouyuService().liveRooms(maxRoomCount)

    // per room per THREAD to fetch bullet screen
    for (i in 1..maxRoomCount) {
        val room = rooms.get(i)
        val room_id = room.room_id
        val room_name = room.room_name

        Thread(Runnable {
            val douyuService = DouyuService()
            douyuService.connect(room_id, groupId)
            HeartbeatService(douyuService).start()
            PullMessageService(douyuService).start()
        }).start();

    }

    // socket connect and data prepare
    //    DouyuService.connect(roomId, groupId)
    // keep alive
    //    HeartbeatService.start()
    // pull message
    //    PullMessageService.start()
}