package robott.dy.model

/**
 * Douyu socket data protocol encoder
 * Created by linpeng on 2016/3/30.
 */
class DouyuEncoder {

    val buf: StringBuffer? = StringBuffer();

    /**
     * 数据包末尾必须以'\0'结尾
     */
    fun getResult(): String = buf?.append(0.toChar()).toString()

    /**
     * add data item
     */
    fun addItem(key: String, value: Any): DouyuEncoder {
        //根据斗鱼弹幕协议进行相应的编码处理
        buf?.append(key.replace("/".toRegex(), "@S").replace("@".toRegex(), "@A"))
        buf?.append("@=")
        if (value is String) {
            buf?.append(value.replace("/".toRegex(), "@S").replace("@".toRegex(), "@A"))
        } else if (value is Int) {
            buf?.append(value)
        }
        buf?.append("/")
        return this
    }
}