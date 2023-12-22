package com.hal.kaiyan.pagingresource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.hal.kaiyan.entity.CategoryDynamicsData
import com.hal.kaiyan.net.RetrofitClient
import com.hal.kaiyan.net.ServicesConfig

/**
 * ...
 * @author LeiHao
 * @date 2023/12//11
 * @description 分类动态界面
 */

class CategoryDynamicsPagingSource(private val tagId: String) :
    PagingSource<Int, CategoryDynamicsData>() {

    /**
     * 下一次请求时的参数，即nextPageUrl中的 start 、num
     */
    private var nextParams = ""
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CategoryDynamicsData> {
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
                    .getCategoryDynamics("", "", tagId)
            } else {
                RetrofitClient.getService(ServicesConfig::class.java).getCategoryDynamics(
                    nextParams.split("&")[0].split("=")[1],
                    nextParams.split("&")[1].split("=")[1],
                    nextParams.split("&")[2].split("=")[1]
                )
            }

            /**
             * nextPageUrl为空则赋予 nextParams 为空
             * 不为空则计算得到 nextParams，即这样的字符串："start=10&num=10&id=14"，用于加载下一页
             */
            nextParams = if (baseResp.nextPageUrl != null) {
                baseResp.nextPageUrl.substring(
                    48, baseResp.nextPageUrl.length
                )
            } else ""

            /**
             * 得到分类推荐数据
             */
            val realData = mutableListOf<CategoryDynamicsData>()
            for (item in baseResp.itemList!!) {
                item.data.content.data.run {
                    realData.add(
                        CategoryDynamicsData(
                            id,
                            cover.feed,
                            description,
                            owner?.avatar ?: "",
                            owner?.nickname ?: "",
                            if (resourceType == "ugc_video") 2 else 0,
                            releaseTime,
                            CategoryDynamicsData.Consumption(
                                consumption.collectionCount,
                                consumption.shareCount,
                                consumption.replyCount
                            ),
                            if (resourceType != "ugc_video") urls else null,
                            if (resourceType == "ugc_video") playUrl else null
                        )
                    )
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

    override fun getRefreshKey(state: PagingState<Int, CategoryDynamicsData>): Int? = null
}
