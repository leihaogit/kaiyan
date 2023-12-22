package com.hal.kaiyan.entity


/**
 * ...
 * @author LeiHao
 * @date 2023/12//11
 * @description 自定义提取出的广场动态页面每一个Item的实体类
 */

data class CategoryDynamicsData(
    val id: Int,
    val coverUrl: String,
    val description: String,
    val headerUrl: String,
    val nickname: String,
    val type: Int, // 类型 0 单图片 1 多图片 2 视频
    val releaseDate: Long,
    val consumption: Consumption,
    val picUrls: List<String>?,
    val playUrl: String?
) {
    data class Consumption(
        val collectionCount: Int,
        val shareCount: Int,
        val replyCount: Int,
    )
}