package com.hal.kaiyan.adapter

import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.hal.kaiyan.R
import com.hal.kaiyan.databinding.CellCategoryDynamicsItemBinding
import com.hal.kaiyan.entity.CategoryDynamicsData
import com.hal.kaiyan.utils.releaseDateToStr


/**
 * ...
 * @author LeiHao
 * @date 2023/12/7
 * @description 广场界面的数据适配器
 */

class CategoryDynamicsPagingAdapter :
    PagingDataAdapter<CategoryDynamicsData, CategoryDynamicsPagingAdapter.MyViewHolder>(diffCallback) {
    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<CategoryDynamicsData>() {
            override fun areItemsTheSame(
                oldItem: CategoryDynamicsData, newItem: CategoryDynamicsData
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: CategoryDynamicsData, newItem: CategoryDynamicsData
            ): Boolean {
                return oldItem == newItem
            }

        }
    }

    fun getCurrentData(): List<CategoryDynamicsData> {
        return snapshot().items
    }

    interface OnItemClickListener {
        fun onItemClick(categoryDynamicsData: CategoryDynamicsData, view: View)
    }

    private var listener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val categoryDynamicsData = getItem(position)

        when (val binding = holder.binding) {
            is CellCategoryDynamicsItemBinding -> {
                if (categoryDynamicsData != null) {
                    //开启闪烁
                    binding.shimmerLayout.apply {
                        setShimmerColor(0x55FFFFFF)
                        setShimmerAngle(0)
                        startShimmerAnimation()
                    }
                    //cover图
                    binding.imageViewCover.let {
                        Glide.with(it).load(categoryDynamicsData.coverUrl).placeholder(
                            ColorDrawable(
                                ContextCompat.getColor(
                                    binding.root.context, R.color.placeholder_color
                                )
                            )
                        ).listener(object : RequestListener<Drawable> {
                            override fun onLoadFailed(
                                e: GlideException?,
                                model: Any?,
                                target: Target<Drawable>?,
                                isFirstResource: Boolean
                            ): Boolean {
                                return false
                            }

                            override fun onResourceReady(
                                resource: Drawable?,
                                model: Any?,
                                target: Target<Drawable>?,
                                dataSource: DataSource?,
                                isFirstResource: Boolean
                            ): Boolean {
                                return false.also { binding.shimmerLayout.stopShimmerAnimation() }
                            }

                        }).into(it)
                    }
                    //视频作者
                    binding.textViewAuthor.text = categoryDynamicsData.nickname
                    //作者头像
                    binding.imageViewAuthorHeader.let {
                        Glide.with(it).load(categoryDynamicsData.headerUrl).into(
                            it
                        )
                    }
                    //视频描述
                    categoryDynamicsData.description.let {
                        if (it.isEmpty()) {
                            binding.textViewDescription.isVisible = false
                        } else binding.textViewDescription.text = it
                    }
                    //视频点赞
                    binding.textViewCollectionCount.text =
                        categoryDynamicsData.consumption.collectionCount.toString()
                    //视频转发
                    binding.textViewShareCount.text =
                        categoryDynamicsData.consumption.shareCount.toString()
                    //视频评论
                    binding.textViewReplyCount.text =
                        categoryDynamicsData.consumption.replyCount.toString()
                    //发布时间
                    binding.textViewReleaseDate.text =
                        categoryDynamicsData.releaseDate.releaseDateToStr()

                    //视频播放提示是否显示
                    binding.imageView.isVisible = categoryDynamicsData.playUrl != null

                    binding.imageViewCover.setOnClickListener {
                        listener?.onItemClick(categoryDynamicsData, it)
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = CellCategoryDynamicsItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return MyViewHolder(binding)
    }

    class MyViewHolder(val binding: ViewBinding) : RecyclerView.ViewHolder(binding.root)
}