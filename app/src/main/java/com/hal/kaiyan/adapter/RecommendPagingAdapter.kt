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
import com.hal.kaiyan.databinding.CellVideoItemBinding
import com.hal.kaiyan.entity.VideoInfoData
import com.hal.kaiyan.utils.durationToStr


/**
 * ...
 * @author LeiHao
 * @date 2023/12/5
 * @description 视频显示页面的通用分页适配器
 */

class RecommendPagingAdapter :
    PagingDataAdapter<VideoInfoData, RecommendPagingAdapter.MyViewHolder>(diffCallback) {
    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<VideoInfoData>() {
            override fun areItemsTheSame(
                oldItem: VideoInfoData, newItem: VideoInfoData
            ): Boolean {
                return false
            }

            override fun areContentsTheSame(
                oldItem: VideoInfoData, newItem: VideoInfoData
            ): Boolean {
                return false
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

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val videoInfoData = getItem(position)
        when (val binding = holder.binding) {
            is CellVideoItemBinding -> {
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
                    //作者头像
                    binding.imageViewAuthorHeader.let {
                        Glide.with(it).load(videoInfoData.authorHeader).into(
                            it
                        )
                    }
                    //视频时长
                    binding.textViewDuration.text = videoInfoData.duration.durationToStr()
                    //视频种类
                    binding.textViewKind.text =
                        binding.root.context.resources.getString(R.string.kind, videoInfoData.kind)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            CellVideoItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    class MyViewHolder(val binding: ViewBinding) : RecyclerView.ViewHolder(binding.root)

}