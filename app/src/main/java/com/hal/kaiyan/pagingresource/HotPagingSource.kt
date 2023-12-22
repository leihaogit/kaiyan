package com.hal.kaiyan.pagingresource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.hal.kaiyan.entity.ItemList
import com.hal.kaiyan.entity.VideoInfoData
import com.hal.kaiyan.net.RetrofitClient
import com.hal.kaiyan.net.ServicesConfig


/**
 * ...
 * @author LeiHao
 * @date 2023/12/11
 * @description 热门页面用于分页的PagingResource
 */

class HotPagingSource(private val strategy: String) : PagingSource<Int, VideoInfoData>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, VideoInfoData> {
        return try {
            /**
             * 接口只返回一页，就不做分页处理了
             */
            val page = params.key ?: 1
            val baseResp = RetrofitClient.getService(ServicesConfig::class.java).getHot(strategy)
            val realData = mutableListOf<VideoInfoData>()
            for (item in baseResp.itemList!!) {
                realData.add(swapToVideoInfoData(item.data))
            }
            val prevKey = if (page > 1) page - 1 else null
            val nextKey = null
            LoadResult.Page(realData, prevKey, nextKey)
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }

    private fun swapToVideoInfoData(data: ItemList.Data) = data.run {
        VideoInfoData(
            id,
            playUrl,
            cover.feed,
            cover.blurred,
            title,
            category,
            description,
            VideoInfoData.Consumption(
                consumption.collectionCount, consumption.shareCount, consumption.replyCount
            ),
            author?.name,
            author?.description,
            author?.icon,
            duration,
            releaseTime,
            null
        )
    }

    override fun getRefreshKey(state: PagingState<Int, VideoInfoData>): Int? = null
}
