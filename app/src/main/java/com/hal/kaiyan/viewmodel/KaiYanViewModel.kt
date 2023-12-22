package com.hal.kaiyan.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.hal.kaiyan.base.BaseResp
import com.hal.kaiyan.entity.CategoryData
import com.hal.kaiyan.entity.CategoryDynamicsData
import com.hal.kaiyan.entity.ItemList
import com.hal.kaiyan.entity.NotificationData
import com.hal.kaiyan.entity.ReplyData
import com.hal.kaiyan.entity.SquareData
import com.hal.kaiyan.entity.VideoInfoData
import com.hal.kaiyan.repository.KaiYanRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch


/**
 * ...
 * @author LeiHao
 * @date 2023/12/5
 * @description 开眼短视频的ViewModel类，用于绑定数据和行为
 */

class KaiYanViewModel : ViewModel() {

    private val kaiYanRepository = KaiYanRepository()

    /**
     * 获取日报数据
     * @return Flow<PagingData<VideoInfoData>>，日报页面的分页数据
     */
    fun getDailyPagingData(): Flow<PagingData<VideoInfoData>> =
        kaiYanRepository.getDailyPagingData().cachedIn(viewModelScope)

    /**
     * 获取推荐数据
     * @return Flow<PagingData<VideoInfoData>>，推荐页面的分页数据
     */
    fun getRecommendPagingData(): Flow<PagingData<VideoInfoData>> =
        kaiYanRepository.getRecommendPagingData().cachedIn(viewModelScope)

    /**
     * 获取广场相关数据
     * @return Flow<PagingData<RecData>>，广场页面的分页数据
     */
    fun getSquarePagingData(): Flow<PagingData<SquareData>> =
        kaiYanRepository.getSquarePagingData().cachedIn(viewModelScope)

    /**
     * 获取视频内容相关数据
     */

    //不可见的videoRelatedData副本
    private val _videoRelatedData = MutableLiveData<BaseResp<List<ItemList>>>()

    //可见的videoRelatedData，用于观察
    val videoRelatedData: LiveData<BaseResp<List<ItemList>>>
        get() = _videoRelatedData

    //用于获取视频相关数据的方法
    fun getVideoRelatedData(videoId: String) = viewModelScope.launch {
        kaiYanRepository.getRelatedData(videoId).collect {
            _videoRelatedData.value = it
        }
    }

    /**
     * 获取分类信息
     */
    fun getCategoryPagingData(): Flow<PagingData<CategoryData>> =
        kaiYanRepository.getCategoryPagingData().cachedIn(viewModelScope)

    /**
     * 获取通知内容相关数据
     */
    fun getNotificationPagingData(): Flow<PagingData<NotificationData>> =
        kaiYanRepository.getNotificationPagingData().cachedIn(viewModelScope)

    /**
     * 获取评论相关数据
     */
    fun getReplyPagingData(videoId: String): Flow<PagingData<ReplyData>> =
        kaiYanRepository.getReplyPagingData(videoId).cachedIn(viewModelScope)


    /**
     * 获取热门数据
     */
    fun getHotPagingData(strategy: String): Flow<PagingData<VideoInfoData>> =
        kaiYanRepository.getHotPagingData(strategy).cachedIn(viewModelScope)


    /**
     * 获取分类推荐数据
     */
    fun getCategoryRecommendPagingData(tagId: String): Flow<PagingData<VideoInfoData>> =
        kaiYanRepository.getCategoryRecommendPagingData(tagId).cachedIn(viewModelScope)

    /**
     * 获取分类动态
     */

    fun getCategoryDynamicsPagingData(tagId: String): Flow<PagingData<CategoryDynamicsData>> =
        kaiYanRepository.getCategoryDynamicsPagingData(tagId).cachedIn(viewModelScope)

    /**
     * 获取本地收藏数据，分页形式
     */
    fun getVideosPagingData(): Flow<PagingData<VideoInfoData>> =
        kaiYanRepository.getVideosPagingData().cachedIn(viewModelScope)

    /**
     * 获取有没有某一个指定数据
     */
    fun hasVideo(id: Int): LiveData<Int> {
        return kaiYanRepository.hasVideo(id)
    }

    /**
     * 插入收藏数据
     */
    suspend fun insertVideo(videoInfoData: VideoInfoData) {
        kaiYanRepository.insertVideo(videoInfoData)
    }

    /**
     * 删除收藏数据
     */
    suspend fun deleteVideo(videoInfoData: VideoInfoData) {
        kaiYanRepository.deleteVideo(videoInfoData)
    }

    /**
     * 删除所有收藏数据
     */
    suspend fun deleteAllVideos() {
        kaiYanRepository.deleteAllVideos()
    }

    /**
     * 查看有多少条数据
     */
    fun hasVideoCount(): LiveData<Int> {
        return kaiYanRepository.hasVideoCount()
    }


}