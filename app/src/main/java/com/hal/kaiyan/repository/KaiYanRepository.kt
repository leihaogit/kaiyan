package com.hal.kaiyan.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import com.hal.kaiyan.base.BaseResp
import com.hal.kaiyan.database.VideoDatabase
import com.hal.kaiyan.entity.VideoInfoData
import com.hal.kaiyan.net.DataState
import com.hal.kaiyan.net.RetrofitClient
import com.hal.kaiyan.net.ServicesConfig
import com.hal.kaiyan.pagingresource.CategoryDynamicsPagingSource
import com.hal.kaiyan.pagingresource.CategoryPagingSource
import com.hal.kaiyan.pagingresource.CategoryRecommendPagingSource
import com.hal.kaiyan.pagingresource.DailyPagingSource
import com.hal.kaiyan.pagingresource.HotPagingSource
import com.hal.kaiyan.pagingresource.NotificationPagingSource
import com.hal.kaiyan.pagingresource.RecommendPagingSource
import com.hal.kaiyan.pagingresource.ReplyPagingSource
import com.hal.kaiyan.pagingresource.SquarePagingSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext


/**
 * ...
 * @author LeiHao
 * @date 2023/12/5
 * @description 开眼短视频的仓库类，封装底层获取数据的操作
 */

class KaiYanRepository {

    private val videoDao = VideoDatabase.instance.getVideoDao()

    /**
     * 获取分页数据基本配置类
     */
    private fun <T : Any> getPagingData(pagingSourceFactory: () -> PagingSource<Int, T>): Flow<PagingData<T>> {
        return Pager(config = PagingConfig(
            pageSize = 10, prefetchDistance = 10
        ), pagingSourceFactory = { pagingSourceFactory() }).flow
    }

    /**
     * 获取非分页数据的的公共 Flow
     */
    private inline fun <T : Any> executeResp(crossinline block: suspend () -> BaseResp<T>): Flow<BaseResp<T>> =
        flow {
            emit(BaseResp(dataState = DataState.LOADING))
            try {
                val resp = block()
                resp.dataState = DataState.SUCCESS
                emit(resp)
            } catch (e: Exception) {
                emit(BaseResp(dataState = DataState.ERROR, error = e))
            } finally {
                emit(BaseResp(dataState = DataState.COMPLETED))
            }
        }


    /**
     * 获取日报数据
     */
    fun getDailyPagingData() = getPagingData { DailyPagingSource() }

    /**
     * 获取推荐数据
     */
    fun getRecommendPagingData() = getPagingData { RecommendPagingSource() }

    /**
     * 获取视频相关数据
     * @param  videoId 视频id
     */
    fun getRelatedData(videoId: String) = executeResp {
        RetrofitClient.getService(ServicesConfig::class.java).getRelated(videoId)
    }

    /**
     * 获取广场相关数据
     */
    fun getSquarePagingData() = getPagingData { SquarePagingSource() }

    /**
     * 获取分类数据
     */
    fun getCategoryPagingData() = getPagingData { CategoryPagingSource() }

    /**
     * 获取通知相关数据
     */
    fun getNotificationPagingData() = getPagingData { NotificationPagingSource() }

    /**
     * 获取评论相关数据
     */
    fun getReplyPagingData(videoId: String) = getPagingData { ReplyPagingSource(videoId) }

    /**
     * 获取热门数据
     * @param strategy 周，月还是总
     */
    fun getHotPagingData(strategy: String) = getPagingData { HotPagingSource(strategy) }

    /**
     * 获取分类推荐数据
     */
    fun getCategoryRecommendPagingData(tagId: String) =
        getPagingData { CategoryRecommendPagingSource(tagId) }

    /**
     * 获取分类动态数据
     */
    fun getCategoryDynamicsPagingData(tagId: String) =
        getPagingData { CategoryDynamicsPagingSource(tagId) }


    /**
     * 获取所有的本地数据
     */
    fun getVideosPagingData() = getPagingData { videoDao.getVideos() }

    /**
     * 查看有没有某一个video
     */
    fun hasVideo(id: Int) = videoDao.hasVideo(id)

    /**
     * 插入收藏数据
     */
    suspend fun insertVideo(videoInfoData: VideoInfoData) {
        withContext(Dispatchers.IO) {
            videoDao.insertVideos(videoInfoData)
        }
    }

    /**
     * 删除收藏数据
     */
    suspend fun deleteVideo(videoInfoData: VideoInfoData) {
        withContext(Dispatchers.IO) {
            videoDao.deleteVideos(videoInfoData)
        }
    }

    /**
     * 删除所有收藏数据
     */
    suspend fun deleteAllVideos() {
        withContext(Dispatchers.IO) {
            videoDao.deleteAllVideos()
        }
    }


    /**
     * 查看有多少条数据
     */
    fun hasVideoCount() = videoDao.hasVideoCount()
}