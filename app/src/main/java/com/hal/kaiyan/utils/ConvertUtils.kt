package com.hal.kaiyan.utils

import android.content.res.Resources
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.roundToInt

/**
 * @author LeiHao
 * @date 2023/10/25
 * @description 用来进行格式或者尺寸之间的转换
 */

/**
 * dp转px
 */
fun Int.dpToPx(): Int {
    return (Resources.getSystem().displayMetrics.density * this + 0.5f).roundToInt()
}

/**
 * sp转px
 */
fun Int.spToPx(): Int {
    return (Resources.getSystem().displayMetrics.scaledDensity * this + 0.5f).roundToInt()
}

/**
 * px转dp
 */
fun Int.pxToDp(): Int {
    return (this / Resources.getSystem().displayMetrics.density + 0.5f).roundToInt()
}

/**
 * px转sp
 */
fun Int.pxToSp(): Int {
    return (this / Resources.getSystem().displayMetrics.scaledDensity + 0.5f).roundToInt()
}

/**
 * 视频时长转换
 */
fun Int.durationToStr(): String {
    val seconds = this % 60
    val minute = this / 60
    return if (seconds < 10) {
        if (minute < 10) "0$minute:0$seconds"
        else "$minute:0$seconds"
    } else {
        if (minute < 10) "0$minute:$seconds"
        else "$minute:$seconds"
    }
}

/**
 * 视频发布时间转换
 */
fun Long.releaseDateToStr(): String {
    val dateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.getDefault())
    return dateFormat.format(Date(this))
}

/**
 * 评论时间转换
 */
fun Long.replyDateToStr(): String {
    val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    return dateFormat.format(Date(this))
}

/**
 * unix时间转日期格式，不含小时和分
 * 如 1669478400000 到 "2022/11/26"
 */
fun Long.dateToStr(): String {
    val dateFormat = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
    return dateFormat.format(Date(this))
}

/**
 * 将日期字符串转换为对应的时间戳
 * 如 "2022/11/26" - 1669478400000
 * @return 对应的时间戳
 */
fun String.strToDate(): Long {
    val dateFormat = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
    return try {
        dateFormat.parse(this)?.time ?: -1
    } catch (e: ParseException) {
        -1
    }
}

/**
 * Byte 转 MB
 */
fun Long.byteToMb(): String {
    val mb = this.toDouble() / (1024 * 1024)
    return String.format("%.2f MB", mb)
}