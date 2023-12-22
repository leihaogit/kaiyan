package com.hal.kaiyan.pagingresource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.hal.kaiyan.entity.SquareData
import com.hal.kaiyan.entity.SquareEntity
import com.hal.kaiyan.net.RetrofitClient
import com.hal.kaiyan.net.ServicesConfig
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.temporal.ChronoUnit
import java.util.concurrent.ThreadLocalRandom


/**
 * ...
 * @author LeiHao
 * @date 2023/12/7
 * @description 广场页面用于分页的PagingResource
 */

class SquarePagingSource : PagingSource<Int, SquareData>() {

    /**
     * 下一次请求时的参数，即nextPageUrl中的 startScore 、pageCount
     */
    private var nextParams = ""
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, SquareData> {
        return try {
            /**
             * 表示当前加载的页数
             */
            val page = params.key ?: 1

            /**
             * 请求得到的内容，如果是 nextParams 为空即如果是第一次响应，则发起首页请求，默认一次15条数据
             * 否则，发起后续分页请求，请求的参数通过 nextParams 获得
             */
            val startDate = LocalDate.of(2019, 1, 1)
            val endDate = LocalDate.of(2022, 8, 31)
            val randomDate = startDate.plusDays(
                ThreadLocalRandom.current().nextLong(ChronoUnit.DAYS.between(startDate, endDate))
            )
            val randomTimestamp = randomDate.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()
            val baseResp = if (nextParams == "") {
                RetrofitClient.getService(ServicesConfig::class.java)
                    .getRec(randomTimestamp.toString(), "3")
            } else {
                RetrofitClient.getService(ServicesConfig::class.java).getRec(
                    nextParams.split("&")[0].split("=")[1],
                    nextParams.split("&")[1].split("=")[1],
                )
            }

            /**
             * nextPageUrl为空则赋予 nextParams 为空
             * 不为空则计算得到 nextParams，即这样的字符串："startScore=1661712555000&pageCount=2"，用于加载下一页
             */
            nextParams = if (baseResp.nextPageUrl != null) {
                baseResp.nextPageUrl.substring(
                    53, baseResp.nextPageUrl.length
                )
            } else ""


            /**
             * 得到广场卡片条目数据
             */
            val realData = mutableListOf<SquareData>()
            for (item in baseResp.itemList!!) {
                when (item.type) {
                    "communityColumnsCard" -> {
                        realData.add(swapToRecData(item.data.content.data))
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
     * 转换为RecData对象
     */
    private fun swapToRecData(data: SquareEntity.Data.Content.Data): SquareData {
        val id = data.id
        val coverUrl = data.cover.feed
        val description = data.description
        val avatar = data.owner?.avatar
        val nickname = data.owner?.nickname
        val consumption = SquareData.Consumption(
            data.consumption.collectionCount,
            data.consumption.shareCount,
            data.consumption.replyCount,
        )
        val type = when (data.resourceType) {
            "ugc_picture" -> if (data.urls.size == 1) 0 else 1
            "ugc_video" -> 2
            else -> -1
        }
        val picUrls = if (data.resourceType == "ugc_picture") data.urls else null
        val playUrl = if (data.resourceType == "ugc_video") data.playUrl else null
        val releaseTime = data.releaseTime

        return SquareData(
            id,
            coverUrl,
            description,
            avatar ?: "",
            nickname ?: "",
            consumption,
            type,
            picUrls,
            playUrl,
            releaseTime
        )
    }

    override fun getRefreshKey(state: PagingState<Int, SquareData>): Int? = null
}
