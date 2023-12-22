package com.hal.kaiyan.entity

/**
 * ...
 * @author LeiHao
 * @date 2023/12/12
 * @description 播放视频图片界面的资源类
 */

data class SourceData(
    val picUrls: List<String>?, //图片列表，资源是视频时为空
    val consumption: Consumption, //图片点赞数
    val playUrl: String?,  //视频链接，资源为图片时为空
    val coverUrl: String?,  //视频cover图，资源为图片时为空
    val type: String, //类型 - "photo" or "video"
    val date: Long, //时间
    val description: String, //描述
    val authorHeader: String, //作者头像
    val nickname: String //作者昵称
) {
    data class Consumption(
        val collectionCount: Int,
        val shareCount: Int,
        val replyCount: Int,
    )
}