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
import com.hal.kaiyan.databinding.CellCategoryItemBinding
import com.hal.kaiyan.entity.CategoryData

/**
 * ...
 * @author LeiHao
 * @date 2023/12/12
 * @description 分类界面的数据适配器
 */

class CategoryPagingAdapter :
    PagingDataAdapter<CategoryData, CategoryPagingAdapter.MyViewHolder>(diffCallback) {
    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<CategoryData>() {
            override fun areItemsTheSame(
                oldItem: CategoryData, newItem: CategoryData
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: CategoryData, newItem: CategoryData
            ): Boolean {
                return oldItem == newItem
            }

        }
    }

    interface OnItemClickListener {
        fun onItemClick(categoryData: CategoryData, view: View)
    }

    private var listener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val categoryEntity = getItem(position)
        when (val binding = holder.binding) {
            is CellCategoryItemBinding -> {
                if (categoryEntity != null) {
                    //接口的图片不能用，只能自己加cover图片了
                    when (categoryEntity.name) {
                        "广告" -> {
                            categoryEntity.coverUrl =
                                "https://images.pexels.com/photos/11124447/pexels-photo-11124447.jpeg?auto=compress&cs=tinysrgb&w=400"
                        }

                        "剧情" -> {
                            categoryEntity.coverUrl =
                                "https://images.pexels.com/photos/4170628/pexels-photo-4170628.jpeg?auto=compress&cs=tinysrgb&w=400"
                        }

                        "运动" -> {
                            categoryEntity.coverUrl =
                                "https://images.pexels.com/photos/11644798/pexels-photo-11644798.jpeg?auto=compress&cs=tinysrgb&w=400"
                        }

                        "创意" -> {
                            categoryEntity.coverUrl =
                                "https://images.pexels.com/photos/11082996/pexels-photo-11082996.jpeg?auto=compress&cs=tinysrgb&w=400"
                        }

                        "旅行" -> {
                            categoryEntity.coverUrl =
                                "https://images.pexels.com/photos/1869643/pexels-photo-1869643.jpeg?auto=compress&cs=tinysrgb&w=400"
                        }

                        "影视" -> {
                            categoryEntity.coverUrl =
                                "https://images.pexels.com/photos/1983037/pexels-photo-1983037.jpeg?auto=compress&cs=tinysrgb&w=400"
                        }

                        "记录" -> {
                            categoryEntity.coverUrl =
                                "https://images.pexels.com/photos/7932651/pexels-photo-7932651.jpeg?auto=compress&cs=tinysrgb&w=400"
                        }

                        "音乐" -> {
                            categoryEntity.coverUrl =
                                "https://images.pexels.com/photos/9980327/pexels-photo-9980327.jpeg?auto=compress&cs=tinysrgb&w=400"
                        }

                        "科技" -> {
                            categoryEntity.coverUrl =
                                "https://images.pexels.com/photos/18311171/pexels-photo-18311171.jpeg?auto=compress&cs=tinysrgb&w=400"
                        }

                        "开胃" -> {
                            categoryEntity.coverUrl =
                                "https://images.pexels.com/photos/1055270/pexels-photo-1055270.jpeg?auto=compress&cs=tinysrgb&w=400"
                        }

                        "游戏" -> {
                            categoryEntity.coverUrl =
                                "https://images.pexels.com/photos/9704415/pexels-photo-9704415.jpeg?auto=compress&cs=tinysrgb&w=400"
                        }

                        "动画" -> {
                            categoryEntity.coverUrl =
                                "https://images.pexels.com/photos/14197884/pexels-photo-14197884.jpeg?auto=compress&cs=tinysrgb&w=400"
                        }

                        "搞笑" -> {
                            categoryEntity.coverUrl =
                                "https://images.pexels.com/photos/6970700/pexels-photo-6970700.jpeg?auto=compress&cs=tinysrgb&w=400"
                        }

                        "时尚" -> {
                            categoryEntity.coverUrl =
                                "https://images.pexels.com/photos/16805249/pexels-photo-16805249.jpeg?auto=compress&cs=tinysrgb&w=400"
                        }

                        "生活" -> {
                            categoryEntity.coverUrl =
                                "https://images.pexels.com/photos/18273370/pexels-photo-18273370.jpeg?auto=compress&cs=tinysrgb&w=400"
                        }

                        "综艺" -> {
                            categoryEntity.coverUrl =
                                "https://images.pexels.com/photos/9889178/pexels-photo-9889178.jpeg?auto=compress&cs=tinysrgb&w=400"
                        }

                        "萌宠" -> {
                            categoryEntity.coverUrl =
                                "https://images.pexels.com/photos/14720760/pexels-photo-14720760.jpeg?auto=compress&cs=tinysrgb&w=400"
                        }

                        else -> {
                            categoryEntity.coverUrl =
                                "https://images.pexels.com/photos/179222/pexels-photo-179222.jpeg?auto=compress&cs=tinysrgb&w=400"
                        }
                    }

                    binding.textViewName.text = categoryEntity.name
                    binding.imageViewCover.let {
                        Glide.with(it).load(categoryEntity.coverUrl).placeholder(
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
                    binding.root.setOnClickListener {
                        listener?.onItemClick(categoryEntity, binding.imageViewCover)
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            CellCategoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    class MyViewHolder(val binding: ViewBinding) : RecyclerView.ViewHolder(binding.root)
}