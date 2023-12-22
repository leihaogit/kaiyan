package com.hal.kaiyan.entity


/**
 * ...
 * @author LeiHao
 * @date 2023/12/7
 * @description 自定义提取出的广场页面每一个Item的实体类
 */

data class SquareData(
    val id: Int, //id
    val coverUrl: String, // 社区封面图的 URL
    val title: String, // 社区的描述或者说标题
    val headerUrl: String, // 用户头像的 URL
    val nickname: String, // 用户昵称
    val consumption: Consumption, // 评论
    val type: Int, // 类型 0 单图片 1 多图片 2 视频
    val picUrls: List<String>?, // 图片列表的 URL 集合，可能为空
    val playUrl: String?, // 视频播放地址，可能为空
    val date: Long //上传时间
){
    data class Consumption(
        val collectionCount: Int,
        val shareCount: Int,
        val replyCount: Int,
    )
}
