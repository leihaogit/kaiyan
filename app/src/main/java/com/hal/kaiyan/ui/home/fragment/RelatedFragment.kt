package com.hal.kaiyan.ui.home.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.hal.kaiyan.adapter.RelatedAdapter
import com.hal.kaiyan.databinding.FragmentRelatedBinding
import com.hal.kaiyan.entity.ItemList
import com.hal.kaiyan.entity.VideoInfoData
import com.hal.kaiyan.net.DataState
import com.hal.kaiyan.ui.base.BaseFragment
import com.hal.kaiyan.ui.base.Constant
import com.hal.kaiyan.ui.home.activity.PlayVideoActivity
import com.hal.kaiyan.viewmodel.KaiYanViewModel
import kotlinx.coroutines.launch
import java.util.Date

/**
 * @author LeiHao
 * @date 2023/12/19
 * @description 视频相关信息界面
 */

class RelatedFragment : BaseFragment() {

    companion object {
        @JvmStatic
        fun newInstance(videoInfoData: VideoInfoData?) = RelatedFragment().apply {
            arguments = Bundle().apply {
                putSerializable(Constant.VIDEO_INFO_DATA, videoInfoData)
            }
        }
    }

    private val kaiYanViewModel: KaiYanViewModel by viewModels()

    //本界面接收的视频数据
    private var videoInfoData: VideoInfoData? = null

    private lateinit var binding: FragmentRelatedBinding

    //本界面的所有相关数据，包括自身
    private val videos: MutableList<VideoInfoData> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            videoInfoData = it.getSerializable(Constant.VIDEO_INFO_DATA) as VideoInfoData?
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentRelatedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initData()

        initEvent()

    }

    private fun initEvent() {
        binding.refreshLayout.run {
            setOnRefreshListener { requireActivity().finish() }
        }
    }

    private fun initData() {

        //获取视频相关推荐
        bindToAdapter()

    }

    /**
     * 视频相关推荐
     */
    @SuppressLint("NotifyDataSetChanged")
    private fun bindToAdapter() {
        videoInfoData?.let { videos.add(it) }
        val relatedAdapter = RelatedAdapter(videos)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = relatedAdapter

        //跳转到播放页面
        relatedAdapter.setOnItemClickListener(object : RelatedAdapter.OnItemClickListener {
            override fun onItemClick(videoInfoData: VideoInfoData, type: String) {
                vibrate()
                if (type == "reply") {
                    (requireActivity() as PlayVideoActivity).toPosition(1)
                } else if (type == "refresh") {
                    (requireActivity() as PlayVideoActivity).refresh(videoInfoData)
                }
            }
        })

        var hasVideo = 0
        kaiYanViewModel.hasVideo(videoInfoData!!.id).observe(viewLifecycleOwner) {
            hasVideo = it
            videoInfoData!!.collectDate = if (hasVideo == 0) null else Date()
            relatedAdapter.notifyItemChanged(0)
        }
        //点击收藏
        relatedAdapter.setOnCollectClickListener(object : RelatedAdapter.OnCollectClickListener {
            override fun onCollectClick(videoInfoData: VideoInfoData, view: View) {
                lifecycleScope.launch {
                    if (hasVideo == 0) {
                        kaiYanViewModel.insertVideo(videoInfoData.apply {
                            collectDate = Date()
                        })
                        hasVideo = 1
                    } else {
                        kaiYanViewModel.deleteVideo(videoInfoData)
                        hasVideo = 0
                    }
                    relatedAdapter.notifyItemChanged(0)
                }
            }
        })
        kaiYanViewModel.videoRelatedData.observe(viewLifecycleOwner) {
            when (it.dataState) {
                DataState.SUCCESS -> {
                    //拿到视频相关数据了
                    val realData = mutableListOf<VideoInfoData>()
                    it.itemList!!.forEach { item ->
                        when (item.type) {
                            "videoSmallCard" -> {
                                realData.add(swapToVideoInfoData(item.data))
                            }
                        }
                    }
                    videos.addAll(realData)
                    relatedAdapter.notifyDataSetChanged()
                }

                else -> {}
            }
        }

        //获取一下视频相关信息
        kaiYanViewModel.getVideoRelatedData(videoInfoData?.id.toString())
    }

    /**
     * 转换为VideoInfoData对象
     */
    private fun swapToVideoInfoData(data: ItemList.Data) = data.run {
        VideoInfoData(
            id,
            playUrl,
            cover.feed,
            cover.blurred,
            title,
            category,
            description,
            VideoInfoData.Consumption(
                consumption.collectionCount, consumption.shareCount, consumption.replyCount
            ),
            author?.name,
            author?.description,
            author?.icon,
            duration,
            releaseTime,
            null
        )
    }
}