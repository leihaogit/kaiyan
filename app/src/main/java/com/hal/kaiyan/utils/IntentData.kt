package com.hal.kaiyan.utils


/**
 * ...
 * @author LeiHao
 * @date 2023/12/21
 * @description 数据传输工具类，处理Intent携带大量数据时，超过1MB限制出现的异常场景
 */

object IntentData {

    val map = hashMapOf<String, Any>()

    inline fun <reified T : Any> setData(key: String, t: T) {
        map[key] = t
    }

    inline fun <reified T> getData(key: String): T? {
        return map[key] as? T
    }

}