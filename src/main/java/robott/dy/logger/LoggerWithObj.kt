package robott.dy.logger

/**
 * Logger with obj
 * Created by linpeng on 2016/4/3.
 */
interface LoggerWithObj : Logger {
    fun debug(message: String, obj: Any)
    fun info(message: String, obj: Any)
    fun warn(message: String, obj: Any)
    fun error(message: String, obj: Any)
    fun fatal(message: String, obj: Any)
}