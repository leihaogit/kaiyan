package com.hal.kaiyan.pagingresource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.hal.kaiyan.entity.ReplyData
import com.hal.kaiyan.net.RetrofitClient
import com.hal.kaiyan.net.ServicesConfig

/**
 * ...
 * @author LeiHao
 * @date 2023/12/9
 * @description 通知页面用于分页的PagingResource
 */

class ReplyPagingSource(private val videoId: String) : PagingSource<Int, ReplyData>() {

    /**
     * 下一次请求时的参数，即nextPageUrl中的
     */
    private var nextParams = ""
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ReplyData> {
        return try {
            /**
             * 表示当前加载的页数
             */
            val page = params.key ?: 1

            /**
             * 请求得到的内容，如果是 nextParams 为空即如果是第一次响应，则发起首页请求
             * 否则，发起后续分页请求，请求的参数通过 nextParams 获得
             */
            val baseResp = if (nextParams == "") {
                RetrofitClient.getService(ServicesConfig::class.java).getReply("", videoId, "", "")
            } else {
                RetrofitClient.getService(ServicesConfig::class.java).getReply(
                    nextParams.split("&")[0].split("=")[1],
                    nextParams.split("&")[1].split("=")[1],
                    nextParams.split("&")[2].split("=")[1],
                    nextParams.split("&")[3].split("=")[1]
                )
            }

            /**
             * nextPageUrl为空则赋予 nextParams 为空
             * 不为空则计算得到 nextParams，即这样的字符串："lastId=83&videoId=186856&num=10&type=video"，用于加载下一页
             */
            nextParams = if (baseResp.nextPageUrl != null) {
                baseResp.nextPageUrl.substring(
                    49, baseResp.nextPageUrl.length
                )
            } else ""

            /**
             * 得到评论数据
             */
            val realData = mutableListOf<ReplyData>()
            for (item in baseResp.itemList!!) {
                when (item.type) {
                    "reply" -> {
                        if (item.data.dataType == "ReplyBeanForClient") {
                            realData.add(
                                ReplyData(
                                    item.data.id,
                                    item.data.message,
                                    item.data.createTime,
                                    item.data.likeCount,
                                    item.data.user?.nickname ?: "",
                                    item.data.user?.avatar ?: "",
                                )
                            )
                        }
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

    override fun getRefreshKey(state: PagingState<Int, ReplyData>): Int? = null
}
