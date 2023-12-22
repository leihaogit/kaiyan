package com.hal.kaiyan.pagingresource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.hal.kaiyan.base.BaseResp
import com.hal.kaiyan.entity.CategoryData
import com.hal.kaiyan.net.RetrofitClient
import com.hal.kaiyan.net.ServicesConfig

/**
 * ...
 * @author LeiHao
 * @date 2023/12/12
 * @description 分类界面的PagingResource
 */

class CategoryPagingSource : PagingSource<Int, CategoryData>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CategoryData> {
        return try {
            /**
             * 接口只返回一页，就不做分页处理了
             */
            val page = params.key ?: 1
            val baseResp = BaseResp(
                itemList = RetrofitClient.getService(ServicesConfig::class.java).getCategories()
            )
            val realData = mutableListOf<CategoryData>()
            for (item in baseResp.itemList!!) {
                realData.add(item)
            }
            val prevKey = if (page > 1) page - 1 else null
            val nextKey = null
            LoadResult.Page(realData, prevKey, nextKey)
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, CategoryData>): Int? = null
}
