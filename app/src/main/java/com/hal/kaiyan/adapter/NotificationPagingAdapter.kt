package com.hal.kaiyan.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.hal.kaiyan.databinding.CellNotificationItemBinding
import com.hal.kaiyan.entity.NotificationData
import com.hal.kaiyan.utils.releaseDateToStr


/**
 * ...
 * @author LeiHao
 * @date 2023/12/9
 * @description 通知消息界面的适配器
 */

class NotificationPagingAdapter :
    PagingDataAdapter<NotificationData, NotificationPagingAdapter.MyViewHolder>(diffCallback) {
    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<NotificationData>() {
            override fun areItemsTheSame(
                oldItem: NotificationData, newItem: NotificationData
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: NotificationData, newItem: NotificationData
            ): Boolean {
                return oldItem == newItem
            }

        }
    }

    interface OnItemClickListener {
        fun onItemClick(notificationData: NotificationData, view: View)
    }

    private var listener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val notificationData = getItem(position)

        when (val binding = holder.binding) {
            is CellNotificationItemBinding -> {
                if (notificationData != null) {
                    //标题
                    binding.textViewTitle.text = notificationData.title
                    //内容
                    binding.textViewContent.text = notificationData.content
                    //时间
                    binding.textViewDate.text = notificationData.date.releaseDateToStr()
                    binding.root.setOnClickListener {
                        listener?.onItemClick(notificationData, it)
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            CellNotificationItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    class MyViewHolder(val binding: ViewBinding) : RecyclerView.ViewHolder(binding.root)
}