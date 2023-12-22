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
 * @date 2023/12//11
 * @description 分类推荐界面
 */

class CategoryRecommendPagingSource(private val tagId: String) :
    PagingSource<Int, VideoInfoData>() {

    /**
     * 下一次请求时的参数，即nextPageUrl中的 start 、num
     */
    private var nextParams = ""
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, VideoInfoData> {
        return try {
            /**
             * 表示当前加载的页数，即start，num，strategy，id
             */
            val page = params.key ?: 1

            /**
             * 请求得到的内容，如果是 nextParams 为空即如果是第一次响应，则发起首页请求
             * 否则，发起后续分页请求，请求的参数通过 nextParams 获得
             */
            val baseResp = if (nextParams == "") {
                RetrofitClient.getService(ServicesConfig::class.java)
                    .getCategoryRecommend(tagId, "", "", "")
            } else {
                RetrofitClient.getService(ServicesConfig::class.java).getCategoryRecommend(
                    nextParams.split("&")[3].split("=")[1],
                    nextParams.split("&")[0].split("=")[1],
                    nextParams.split("&")[1].split("=")[1],
                    nextParams.split("&")[2].split("=")[1]
                )
            }

            /**
             * nextPageUrl为空则赋予 nextParams 为空
             * 不为空则计算得到 nextParams，即这样的字符串："start=10&num=10&strategy=date&id=14"，用于加载下一页
             */
            nextParams = if (baseResp.nextPageUrl != null) {
                baseResp.nextPageUrl.substring(
                    46, baseResp.nextPageUrl.length
                )
            } else ""

            /**
             * 得到分类推荐数据
             */
            val realData = mutableListOf<VideoInfoData>()
            for (item in baseResp.itemList!!) {
                realData.add(swapToVideoInfoData(item.data.content.data))
            }

            /**
             * 上一页的页数
             */
            val prevKey = if (page > 1) page - 1 else null

            /**
             * 下一页的页数，如果 nextParams 为空了则下一页也为空
             */
            val nextKey = if (nextParams != "") page + 1 else null

            /**
             * 开始分页加载
             */
            LoadResult.Page(realData, prevKey, nextKey)
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }

    /**
     * 转换为VideoInfoData对象
     */
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
