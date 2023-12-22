package com.hal.kaiyan.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.viewbinding.ViewBinding
import com.hal.kaiyan.databinding.CellFooterItemBinding


/**
 * ...
 * @author LeiHao
 * @date 2023/12/5
 * @description 底部加载更多布局的通用适配器
 */

class PagingFooterAdapter(val retry: () -> Unit) :
    LoadStateAdapter<PagingFooterAdapter.MyViewHolder>() {

    class MyViewHolder(val binding: ViewBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup, loadState: LoadState
    ): MyViewHolder {
        val binding =
            CellFooterItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        //如果是瀑布流或者网格，也需要占满底部屏幕
        binding.root.layoutParams?.let { layoutParams ->
            if (layoutParams is StaggeredGridLayoutManager.LayoutParams) {
                layoutParams.isFullSpan = true
            }
        }
        binding.constraintLayoutRetry.setOnClickListener { retry() }
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, loadState: LoadState) {

        when (val binding = holder.binding) {
            is CellFooterItemBinding -> {
                when (loadState) {
                    is LoadState.Loading -> {
                        binding.lottieLoading.isVisible = true
                        binding.constraintLayoutRetry.isVisible = false
                    }

                    is LoadState.NotLoading -> {
                        binding.lottieLoading.isVisible = false
                        binding.constraintLayoutRetry.isVisible = false
                    }

                    else -> {
                        binding.lottieLoading.isVisible = false
                        binding.constraintLayoutRetry.isVisible = true
                    }
                }
            }
        }
    }
}