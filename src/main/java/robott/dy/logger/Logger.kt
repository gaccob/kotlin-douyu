package robott.dy.logger

/**
 * Logger by custom
 * Created by linpeng on 2016/4/3.
 */
interface Logger {
    // DEBUG 调试信息提示
    fun debug(message: String)

    //    INFO 比较重要的调试信息提示
    fun info(message: String)

    //    WARN 可能存在的潜在问题的提示，以及重要的提示信息
    fun warn(message: String)

    //    ERROR 系统发生异常的提示，这里不允许输出重要的提示信息
    fun error(message: String)

    //    FATAL 系统发生了致命的错误的提示，可能导致系统退出
    fun fatal(message: String)
}