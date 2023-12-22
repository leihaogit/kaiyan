package com.hal.kaiyan.pagingresource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.hal.kaiyan.entity.ItemList
import com.hal.kaiyan.entity.VideoInfoData
import com.hal.kaiyan.net.RetrofitClient
import com.hal.kaiyan.net.ServicesConfig
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.temporal.ChronoUnit
import java.util.concurrent.ThreadLocalRandom


/**
 * ...
 * @author LeiHao
 * @date 2023/12/5
 * @description 推荐页面用于分页的PagingResource
 */

class RecommendPagingSource : PagingSource<Int, VideoInfoData>() {

    /**
     * 下一次请求时的参数，即nextPageUrl中的 date 、num
     */
    private var nextParams = ""
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, VideoInfoData> {
        return try {
            /**
             * 表示当前加载的页数
             */
            val page = params.key ?: 1

            /**
             * 请求得到的内容，如果是 nextParams 为空即如果是第一次响应，则发起首页请求，这里为 2015/06/01 至今随机一天
             * 否则，发起后续分页请求，请求的参数通过 nextParams 获得
             */
            val startDate = LocalDate.of(2015, 6, 1)
            val endDate = LocalDate.now()
            val randomDate = startDate.plusDays(
                ThreadLocalRandom.current().nextLong(ChronoUnit.DAYS.between(startDate, endDate))
            )
            val randomTimestamp = randomDate.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()
            val baseResp = if (nextParams == "") {
                RetrofitClient.getService(ServicesConfig::class.java)
                    .getFeed(randomTimestamp.toString(), "2")
            } else {
                RetrofitClient.getService(ServicesConfig::class.java).getFeed(
                    nextParams.split("&")[0].split("=")[1],
                    nextParams.split("&")[1].split("=")[1],
                )
            }
            /**
             * nextPageUrl为空则赋予 nextParams 为空
             * 不为空则计算得到 nextParams，即这样的字符串："date=1701565200000&num=2"，用于加载下一页
             */
            nextParams = if (baseResp.nextPageUrl != null) {
                baseResp.nextPageUrl.substring(
                    50, baseResp.nextPageUrl.length
                )
            } else ""

            /**
             * 得到视频条目数据
             */
            val realData = mutableListOf<VideoInfoData>()
            for (item in baseResp.itemList!!) {
                when (item.type) {
                    "followCard" -> {
                        realData.add(swapToVideoInfoData(item.data.content.data))
                    }
                }
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
