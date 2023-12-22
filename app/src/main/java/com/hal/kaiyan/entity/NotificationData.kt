package com.hal.kaiyan.entity

/**
 * ...
 * @author LeiHao
 * @date 2023/12/9
 * @description 自定义通知信息数据
 */

data class NotificationData(
    val id: Int, // 唯一标识符
    val title: String?, // 视频标题，可能为空
    val content: String, // 视频内容
    val date: Long, //时间
    val actionUrl: String //跳转链接
)