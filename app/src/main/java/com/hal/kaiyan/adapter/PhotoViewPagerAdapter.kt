package com.hal.kaiyan.adapter

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.hal.kaiyan.databinding.CellPhotoItemBinding
import com.hal.kaiyan.databinding.CellPhotoVideoItemBinding
import com.hal.kaiyan.utils.goneAlphaAnimation
import com.hal.kaiyan.utils.visibleAlphaAnimation

/**
 * ...
 * @author LeiHao
 * @date 2023/12/12
 * @description 图片浏览器的 Adapter，内部适配器
 */

class PhotoViewPagerAdapter(
    private val urls: List<String>, private val pBinding: CellPhotoVideoItemBinding
) : RecyclerView.Adapter<PhotoViewPagerAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            CellPhotoItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    class MyViewHolder(val binding: ViewBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        when (val binding = holder.binding) {
            is CellPhotoItemBinding -> {
                binding.progressBar.isVisible = true
                Glide.with(holder.itemView).load(urls[position])
                    .listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            return false.also { binding.progressBar.isVisible = false }
                        }

                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: Target<Drawable>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            return false.also { binding.progressBar.isVisible = false }
                        }

                    }).into(binding.photoView)
                binding.photoView.setOnClickListener {
                    if (pBinding.constraintLayoutTop.visibility == View.VISIBLE) {
                        pBinding.constraintLayoutTop.goneAlphaAnimation()
                        pBinding.constraintLayoutBottom.goneAlphaAnimation()
                    } else {
                        pBinding.constraintLayoutTop.visibleAlphaAnimation()
                        pBinding.constraintLayoutBottom.visibleAlphaAnimation()
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int = urls.size
}
