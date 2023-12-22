package com.hal.kaiyan.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.hal.kaiyan.databinding.CellReplyItemBinding
import com.hal.kaiyan.entity.ReplyData
import com.hal.kaiyan.utils.replyDateToStr


/**
 * ...
 * @author LeiHao
 * @date 2023/12/7
 * @description 评论界面的数据适配器
 */

class ReplyPagingAdapter :
    PagingDataAdapter<ReplyData, ReplyPagingAdapter.MyViewHolder>(diffCallback) {
    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<ReplyData>() {
            override fun areItemsTheSame(
                oldItem: ReplyData, newItem: ReplyData
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: ReplyData, newItem: ReplyData
            ): Boolean {
                return oldItem == newItem
            }

        }
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val replyData = getItem(position)

        when (val binding = holder.binding) {
            is CellReplyItemBinding -> {
                if (replyData != null) {
                    //头像
                    binding.imageViewAuthorHeader.let {
                        Glide.with(it).load(replyData.avatar).into(it)
                    }
                    //昵称
                    binding.textViewNickname.text = replyData.nickname
                    //时间
                    binding.textViewDate.text = replyData.createTime.replyDateToStr()
                    //内容
                    binding.textViewMessage.text = replyData.message
                    //点赞数
                    binding.textViewAgreeCount.text = replyData.likeCount.toString()
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            CellReplyItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    class MyViewHolder(val binding: ViewBinding) : RecyclerView.ViewHolder(binding.root)
}