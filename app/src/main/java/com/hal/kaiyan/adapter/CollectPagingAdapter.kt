package com.hal.kaiyan.adapter

import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
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
import com.hal.kaiyan.databinding.CellCollectItemBinding
import com.hal.kaiyan.entity.VideoInfoData
import com.hal.kaiyan.utils.durationToStr
import com.hal.kaiyan.utils.releaseDateToStr


/**
 * ...
 * @author LeiHao
 * @date 2023/12/13
 * @description 本地收藏界面适配器
 */

class CollectPagingAdapter :
    PagingDataAdapter<VideoInfoData, CollectPagingAdapter.MyViewHolder>(diffCallback) {
    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<VideoInfoData>() {
            override fun areItemsTheSame(
                oldItem: VideoInfoData, newItem: VideoInfoData
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: VideoInfoData, newItem: VideoInfoData
            ): Boolean {
                return oldItem == newItem
            }

        }
    }

    interface OnItemClickListener {
        fun onItemClick(videoInfoData: VideoInfoData, view: View)
    }

    private var listener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    interface OnUnCollectClickListener {
        fun onUnCollectClick(videoInfoData: VideoInfoData)
    }

    private var unCollectClickListener: OnUnCollectClickListener? = null

    fun setUnCollectClickListener(unCollectClickListener: OnUnCollectClickListener) {
        this.unCollectClickListener = unCollectClickListener
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val videoInfoData = getItem(position)

        when (val binding = holder.binding) {
            is CellCollectItemBinding -> {
                if (videoInfoData != null) {
                    //点击事件监听
                    binding.root.setOnClickListener {
                        listener?.onItemClick(videoInfoData, binding.imageViewCover)
                    }
                    //视频标题
                    binding.textViewTitle.text = videoInfoData.title
                    //开启闪烁
                    binding.shimmerLayout.apply {
                        setShimmerColor(0x55FFFFFF)
                        setShimmerAngle(0)
                        startShimmerAnimation()
                    }
                    //视频封面
                    binding.imageViewCover.let {
                        Glide.with(it).load(videoInfoData.coverUrl).placeholder(
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
                    binding.textViewAuthor.text = videoInfoData.author
                    //视频时长
                    binding.textViewDuration.text = videoInfoData.duration.durationToStr()
                    //视频时间
                    binding.textViewDate.text = videoInfoData.releaseDate.releaseDateToStr()
                    //取消收藏
                    binding.imageViewUnCollect.setOnClickListener {
                        unCollectClickListener?.onUnCollectClick(videoInfoData)
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            CellCollectItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    class MyViewHolder(val binding: ViewBinding) : RecyclerView.ViewHolder(binding.root)
}