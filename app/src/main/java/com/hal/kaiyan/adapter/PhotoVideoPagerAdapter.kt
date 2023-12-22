package com.hal.kaiyan.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.hal.kaiyan.R
import com.hal.kaiyan.databinding.CellPhotoVideoItemBinding
import com.hal.kaiyan.entity.SourceData
import com.hal.kaiyan.ui.home.activity.PhotoVideoActivity
import com.hal.kaiyan.utils.goneAlphaAnimation
import com.hal.kaiyan.utils.visibleAlphaAnimation

/**
 * ...
 * @author LeiHao
 * @date 2023/12/21
 * @description 图片和视频的基础 ViewPager2 适配器
 */

class PhotoVideoPagerAdapter(
    private val activity: PhotoVideoActivity, private var sourceDataList: List<SourceData>
) : RecyclerView.Adapter<PhotoVideoPagerAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): MyViewHolder {
        val binding =
            CellPhotoVideoItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val binding = holder.binding
        val sourceData = sourceDataList[position]
        //隐藏显示ui
        binding.root.setOnClickListener {
            if (binding.constraintLayoutTop.visibility == View.VISIBLE) {
                binding.constraintLayoutTop.goneAlphaAnimation()
                binding.constraintLayoutBottom.goneAlphaAnimation()
            } else {
                binding.constraintLayoutTop.visibleAlphaAnimation()
                binding.constraintLayoutBottom.visibleAlphaAnimation()
            }
        }
        binding.constraintLayoutTop.visibility = View.VISIBLE
        binding.constraintLayoutBottom.visibility = View.VISIBLE
        binding.viewPager2.visibility = View.GONE
        binding.videoPlayer.visibility = View.GONE

        //点击返回
        binding.imageViewBack.setOnClickListener {
            activity.finish()
        }
        //头像
        Glide.with(binding.root.context).load(sourceData.authorHeader)
            .into(binding.imageViewAuthorHeader)
        //昵称
        binding.textViewAuthor.text = sourceData.nickname
        //描述
        binding.textViewDescription.text = sourceData.description
        binding.textViewDescription.visibility =
            if (sourceData.description == "") View.GONE else View.VISIBLE
        //点赞
        binding.textViewCollectionCount.text = sourceData.consumption.collectionCount.toString()
        //评论
        binding.textViewReplyCount.text = sourceData.consumption.replyCount.toString()
        //分享
        binding.textViewShareCount.text = sourceData.consumption.shareCount.toString()

        when (sourceData.type) {
            //图片
            "photo" -> {
                binding.viewPager2.visibility = View.VISIBLE
                sourceData.picUrls?.run {
                    binding.viewPager2.orientation = ViewPager2.ORIENTATION_HORIZONTAL
                    binding.viewPager2.offscreenPageLimit = 1
                    binding.viewPager2.adapter = PhotoViewPagerAdapter(this, binding)
                    //位置
                    binding.textViewPosition.text = binding.root.context.getString(
                        R.string.position, 1, this.size
                    )

                    binding.viewPager2.registerOnPageChangeCallback(object :
                        ViewPager2.OnPageChangeCallback() {
                        override fun onPageSelected(position: Int) {
                            super.onPageSelected(position)
                            binding.textViewPosition.text = binding.root.context.getString(
                                R.string.position, position + 1, this@run.size
                            )
                        }
                    })
                }
            }

            //视频
            else -> {
                binding.videoPlayer.visibility = View.VISIBLE
                binding.videoPlayer.run {
                    //音频焦点冲突时是否释放
                    isReleaseWhenLossAudio = false
                    //封面
                    val imageView = ImageView(context)
                    imageView.scaleType = ImageView.ScaleType.CENTER_CROP
                    Glide.with(context).load(sourceData.coverUrl).into(imageView)
                    thumbImageView = imageView
                    //点击封面可播放
                    setThumbPlay(true)
                    //不可滑动操作
                    setIsTouchWiget(false)
                    //自动循环
                    isLooping = true
                    //设置播放位置防止错位
                    playPosition = position
                    //设置播放URL
                    setUp(sourceData.playUrl, false, null)
                    //返回键
                    backButton.visibility = View.GONE
                }
            }
        }
    }

    override fun getItemCount() = sourceDataList.size

    class MyViewHolder(val binding: CellPhotoVideoItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}