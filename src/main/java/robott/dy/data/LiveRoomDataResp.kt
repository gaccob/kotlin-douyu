package robott.dy.data

import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.google.gson.Gson

/**
 * LiveRoomData response
 * Created by linpeng on 2016/4/3.
 */
data class LiveRoomDataResp(val error: String, val data: List<LiveRoom>) {

    class Deserializer : ResponseDeserializable<LiveRoomDataResp> {
        override fun deserialize(content: String) = Gson().fromJson(content, LiveRoomDataResp::class.java)
    }
}

data class LiveRoom(val room_id: Int, val room_src: String, val room_name: String, val owner_uid: Int, val online: Int, val nickname: String, val url: String) {

    class Deserializer : ResponseDeserializable<LiveRoom> {
        override fun deserialize(content: String) = Gson().fromJson(content, LiveRoom::class.java)
    }
}