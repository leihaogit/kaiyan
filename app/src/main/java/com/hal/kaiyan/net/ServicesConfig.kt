package com.hal.kaiyan.net

import com.hal.kaiyan.base.BaseNotificationResp
import com.hal.kaiyan.base.BaseResp
import com.hal.kaiyan.entity.CategoryData
import com.hal.kaiyan.entity.ItemList
import com.hal.kaiyan.entity.NotificationData
import com.hal.kaiyan.entity.ReplyEntity
import com.hal.kaiyan.entity.SquareEntity
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * ...
 * @author LeiHao
 * @date 2023/11/30
 * @description 配置各个网络请求接口
 */

interface ServicesConfig {
    /**
     * 推荐 + 日报
     */
    @GET("/api/v5/index/tab/feed")
    suspend fun getFeed(
        @Query("date") date: String, @Query("num") num: String
    ): BaseResp<List<ItemList>>

    /**
     * 视频相关内容
     */
    @GET("/api/v4/video/related")
    suspend fun getRelated(
        @Query("id") id: String
    ): BaseResp<List<ItemList>>

    /**
     * 广场内容 - 图文卡片+视频
     */
    @GET("/api/v7/community/tab/rec")
    suspend fun getRec(
        @Query("startScore") startScore: String, @Query("pageCount") pageCount: String
    ): BaseResp<List<SquareEntity>>

    /**
     * 获取分类信息
     */
    @GET("/api/v4/categories")
    suspend fun getCategories(): List<CategoryData>

    /**
     * 获取通知信息
     */
    @GET("/api/v3/messages")
    suspend fun getNotificationData(
        @Query("start") start: String, @Query("num") num: String
    ): BaseNotificationResp<List<NotificationData>>

    /**
     * 获取视频评论信息
     */
    @GET("/api/v2/replies/video")
    suspend fun getReply(
        @Query("lastId") lastId: String,
        @Query("videoId") videoId: String,
        @Query("num") num: String,
        @Query("type") type: String
    ): BaseResp<List<ReplyEntity>>

    /**
     * 获取热门视频
     */
    @GET("/api/v4/rankList/videos")
    suspend fun getHot(
        @Query("strategy") strategy: String,
    ): BaseResp<List<ItemList>>


    /**
     * 获取分类推荐界面数据
     */
    @GET("/api/v1/tag/videos")
    suspend fun getCategoryRecommend(
        @Query("id") id: String,
        @Query("start") start: String,
        @Query("num") num: String,
        @Query("strategy") strategy: String
    ): BaseResp<List<ItemList>>

    /**
     * 获取分类动态界面数据
     */
    @GET("/api/v6/tag/dynamics")
    suspend fun getCategoryDynamics(
        @Query("start") start: String,
        @Query("num") num: String,
        @Query("id") id: String,
    ): BaseResp<List<SquareEntity>>

}