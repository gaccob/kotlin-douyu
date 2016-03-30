package robott.dy.model

import org.apache.commons.lang3.StringUtils
import java.util.*

/**
 * Douyu bullet socreen data in plain text
 * Created by linpeng on 2016/3/30.
 */

class DouyuMessageView(data: String) {
    var messageList: Map<String, Any> = Collections.emptyMap()

    /**
     * Construction
     */
    init {
        messageList = parseRespond(data)
    }

    /**
     * parse response from server
     */
    private fun parseRespond(data: String): Map<String, Any> {
        val rtnMsg = HashMap<String, Any>()

        //处理数据字符串末尾的'/0字符'
        val tempData = StringUtils.substringBeforeLast(data, "/")

        //对数据字符串进行拆分
        val buff = tempData.split("/".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()

        //分析协议字段中的key和value值
        for (tmp in buff) {
            //获取key值
            val key = StringUtils.substringBefore(tmp, "@=")
            //获取对应的value值
            var value: Any = StringUtils.substringAfter(tmp, "@=")

            //如果value值中包含子序列化值，则进行递归分析
            if (StringUtils.contains(value as String, "@A")) {
                value = value.replace("@S".toRegex(), "/").replace("@A".toRegex(), "@")
                value = this.parseRespond(value)
            }

            //将分析后的键值对添加到信息列表中
            rtnMsg.put(key, value)
        }

        return rtnMsg
    }


}
