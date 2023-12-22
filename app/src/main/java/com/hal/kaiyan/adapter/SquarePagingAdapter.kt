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
import com.hal.kaiyan.databinding.CellSquareItemBinding
import com.hal.kaiyan.entity.SquareData


/**
 * ...
 * @author LeiHao
 * @date 2023/12/7
 * @description 广场界面的数据适配器
 */

class SquarePagingAdapter :
    PagingDataAdapter<SquareData, SquarePagingAdapter.MyViewHolder>(diffCallback) {
    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<SquareData>() {
            override fun areItemsTheSame(
                oldItem: SquareData, newItem: SquareData
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: SquareData, newItem: SquareData
            ): Boolean {
                return oldItem == newItem
            }

        }
    }

    fun getCurrentData(): List<SquareData> {
        return snapshot().items
    }

    interface OnItemClickListener {
        fun onItemClick(squareData: SquareData, view: View)
    }

    private var listener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val squareData = getItem(position)

        when (val binding = holder.binding) {
            is CellSquareItemBinding -> {
                if (squareData != null) {
                    //开启闪烁
                    binding.shimmerLayout.apply {
                        setShimmerColor(0x55FFFFFF)
                        setShimmerAngle(0)
                        startShimmerAnimation()
                    }
                    //cover图
                    binding.imageViewCover.let {
                        Glide.with(it).load(squareData.coverUrl).placeholder(
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
                    //头像
                    binding.imageViewHeader.let {
                        Glide.with(it).load(squareData.headerUrl).into(it)
                    }
                    //点赞数
                    binding.textViewAgreeCount.text =
                        squareData.consumption.collectionCount.toString()
                    //title
                    binding.textViewTitle.text = squareData.title
                    binding.textViewTitle.visibility =
                        if (squareData.title.isEmpty()) View.GONE else View.VISIBLE
                    //昵称
                    binding.textViewNickname.text = squareData.nickname

                    //判断是什么类型，0 单图片 1 多图片 2 视频
                    when (squareData.type) {
                        1 -> {
                            binding.imageViewType.isVisible = true
                            binding.imageViewType.setImageResource(R.drawable.baseline_photo_library_24)
                        }

                        2 -> {
                            binding.imageViewType.isVisible = true
                            binding.imageViewType.setImageResource(R.drawable.baseline_slow_motion_video_24)
                        }

                        else -> {
                            binding.imageViewType.isVisible = false
                        }
                    }
                    binding.imageViewCover.setOnClickListener {
                        listener?.onItemClick(squareData, it)
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            CellSquareItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    class MyViewHolder(val binding: ViewBinding) : RecyclerView.ViewHolder(binding.root)
}