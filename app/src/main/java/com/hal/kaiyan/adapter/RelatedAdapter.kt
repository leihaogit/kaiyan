package com.hal.kaiyan.adapter

import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.hal.kaiyan.R
import com.hal.kaiyan.databinding.CellVideoInfoItemBinding
import com.hal.kaiyan.databinding.CellVideoRelatedItemBinding
import com.hal.kaiyan.entity.VideoInfoData
import com.hal.kaiyan.utils.durationToStr
import com.hal.kaiyan.utils.releaseDateToStr


/**
 * ...
 * @author LeiHao
 * @date 2023/12/6
 * @description 视频播放界面的视频相关信息适配器
 */

class RelatedAdapter(private val videoList: List<VideoInfoData>) :
    RecyclerView.Adapter<RelatedAdapter.MyViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(videoInfoData: VideoInfoData, type: String)
    }

    private var listener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    interface OnCollectClickListener {
        fun onCollectClick(videoInfoData: VideoInfoData, view: View)
    }

    private var clickListener: OnCollectClickListener? = null

    fun setOnCollectClickListener(clickListener: OnCollectClickListener) {
        this.clickListener = clickListener
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) 0 else 1
    }

    override fun getItemCount() = videoList.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val videoInfoData = videoList[position]

        when (val binding = holder.binding) {
            is CellVideoRelatedItemBinding -> {
                //点击事件监听
                binding.root.setOnClickListener {
                    listener?.onItemClick(videoInfoData, "refresh")
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
                //视频种类
                binding.textViewKind.text =
                    binding.root.context.resources.getString(R.string.kind, videoInfoData.kind)
                //视频时间
                binding.textViewDate.text = videoInfoData.releaseDate.releaseDateToStr()
            }

            //本视频信息
            is CellVideoInfoItemBinding -> {
                binding.imageViewCollect.setImageResource((if (videoInfoData.collectDate == null) R.drawable.baseline_favorite_24_1 else R.drawable.baseline_favorite_24))
                binding.textViewCollect.text =
                    if (videoInfoData.collectDate == null) binding.root.context.resources.getString(
                        R.string.collect_info
                    ) else binding.root.context.resources.getString(
                        R.string.collected
                    )
                //加载作者头像和用户名
                binding.textViewAuthor.text = videoInfoData.author
                //作者描述
                binding.textViewAuthorDescription.text = videoInfoData.authorDescription
                //作者头像
                Glide.with(binding.root.context).load(videoInfoData.authorHeader)
                    .into(binding.imageViewAuthorHeader)
                //点击收藏
                binding.constraintLayoutCollect.setOnClickListener {
                    clickListener?.onCollectClick(videoInfoData, it)
                }
                //点击评论
                binding.constraintLayoutReplyCount.setOnClickListener {
                    listener?.onItemClick(videoInfoData, "reply")
                }
                //视频标题
                binding.textViewTitle.text = videoInfoData.title
                //视频种类
                binding.textViewKind.text =
                    binding.root.context.resources.getString(R.string.kind, videoInfoData.kind)
                //发布时间
                binding.textViewReleaseDate.text = videoInfoData.releaseDate.releaseDateToStr()
                //视频描述
                binding.textViewDescription.text = videoInfoData.description
                //点赞数
                binding.textViewCollectionCount.text =
                    videoInfoData.consumption.collectionCount.toString()
                //转发数
                binding.textViewShareCount.text = videoInfoData.consumption.shareCount.toString()
                //评论数
                binding.textViewReplyCount.text = videoInfoData.consumption.replyCount.toString()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return if (viewType == 1) {
            val binding = CellVideoRelatedItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            MyViewHolder(binding)
        } else {
            val binding = CellVideoInfoItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            MyViewHolder(binding)
        }
    }

    class MyViewHolder(val binding: ViewBinding) : RecyclerView.ViewHolder(binding.root)
}