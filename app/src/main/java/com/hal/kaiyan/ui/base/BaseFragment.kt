package com.hal.kaiyan.ui.base

import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.PagingData
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hal.kaiyan.App
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

/**
 * @author LeiHao
 * @date 2023/10/25
 * @description Activity的基类，包含强制竖屏、沉浸式状态栏+状态栏深色字体
 */

abstract class BaseFragment : Fragment() {

    /**
     * 发出轻微的震动
     */
    protected fun vibrate() {
        App.vibrate()
    }

    /**
     * 绑定分页数据到 RecyclerView 中的 Adapter
     */
    protected fun <T : Any, V : RecyclerView.ViewHolder, A : PagingDataAdapter<T, V>> bindAdapterToPaging(
        data: Flow<PagingData<T>>, pagingAdapter: A
    ) {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                data.collect {
                    pagingAdapter.submitData(it)
                }
            }
        }
    }
}