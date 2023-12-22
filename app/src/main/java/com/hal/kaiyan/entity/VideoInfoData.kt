package com.hal.kaiyan.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import java.io.Serializable
import java.util.Date


/**
 * ...
 * @author LeiHao
 * @date 2023/12/5
 * @description 自定义视频信息数据
 */

@Entity(tableName = "video_tb", primaryKeys = ["id"])
data class VideoInfoData(
    @ColumnInfo(name = "id") val id: Int, // 视频的唯一标识符
    @ColumnInfo(name = "play_url") val playUrl: String, // 视频的播放链接
    @ColumnInfo(name = "cover_url") val coverUrl: String, // 视频封面图片链接
    @ColumnInfo(name = "cover_blurred") val coverBlurred: String, // 视频高斯模糊链接
    @ColumnInfo(name = "title") val title: String, // 视频标题
    @ColumnInfo(name = "kind") val kind: String, // 视频分类
    @ColumnInfo(name = "description") val description: String, // 视频描述
    @ColumnInfo(name = "consumption") val consumption: Consumption, // 视频的点赞等信息
    @ColumnInfo(name = "author") val author: String?, // 视频作者的名称
    @ColumnInfo(name = "author_description") val authorDescription: String?, // 视频作者的描述
    @ColumnInfo(name = "author_header") val authorHeader: String?, // 视频作者的头像链接
    @ColumnInfo(name = "duration") val duration: Int, // 视频时长（单位：秒）
    @ColumnInfo(name = "release_date") val releaseDate: Long, // 视频发布日期的时间戳
    @ColumnInfo(name = "collect_date") var collectDate: Date? // 收藏时间，用来排序
) : Serializable {
    data class Consumption(
        @ColumnInfo(name = "collection_count") val collectionCount: Int, // 收藏数量
        @ColumnInfo(name = "share_count") val shareCount: Int, // 分享数量
        @ColumnInfo(name = "reply_count") val replyCount: Int // 回复数量
    ) : Serializable
}
