package com.hal.kaiyan.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.hal.kaiyan.R
import com.hal.kaiyan.adapter.CategoryDynamicsPagingAdapter
import com.hal.kaiyan.adapter.CategoryPagingAdapter
import com.hal.kaiyan.adapter.CategoryRecommendPagingAdapter
import com.hal.kaiyan.adapter.CollectPagingAdapter
import com.hal.kaiyan.adapter.DailyPagingAdapter
import com.hal.kaiyan.adapter.HotPagingAdapter
import com.hal.kaiyan.adapter.NotificationPagingAdapter
import com.hal.kaiyan.adapter.PagingFooterAdapter
import com.hal.kaiyan.adapter.RecommendPagingAdapter
import com.hal.kaiyan.adapter.ReplyPagingAdapter
import com.hal.kaiyan.adapter.SquarePagingAdapter
import com.hal.kaiyan.databinding.FragmentReuseBinding
import com.hal.kaiyan.entity.CategoryData
import com.hal.kaiyan.entity.CategoryDynamicsData
import com.hal.kaiyan.entity.NotificationData
import com.hal.kaiyan.entity.SourceData
import com.hal.kaiyan.entity.SquareData
import com.hal.kaiyan.entity.VideoInfoData
import com.hal.kaiyan.ui.home.activity.CategoryActivity
import com.hal.kaiyan.ui.home.activity.PhotoVideoActivity
import com.hal.kaiyan.ui.home.activity.PlayVideoActivity
import com.hal.kaiyan.ui.home.activity.WebViewActivity
import com.hal.kaiyan.utils.vibrate
import com.hal.kaiyan.view.CustomDialog
import com.hal.kaiyan.viewmodel.KaiYanViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


/**
 * @author LeiHao
 * @date 2023/12/11
 * @description 可重用的用于分页加载的Fragment
 */

class ReusePagingFragment : BaseFragment() {

    companion object {
        @JvmStatic
        fun newInstance(type: String, tagId: String?, videoInfoData: VideoInfoData?) =
            ReusePagingFragment().apply {
                arguments = Bundle().apply {
                    putString(Constant.FRAGMENT_TYPE, type)
                    putString(Constant.TAG_ID, tagId)
                    putSerializable(Constant.VIDEO_INFO_DATA, videoInfoData)
                }
            }
    }

    //获取该Fragment的具体类型
    private var type: String? = null

    //获取该Fragment的具体类型
    private var tagId: String? = null

    //本界面接收的视频数据
    private var videoInfoData: VideoInfoData? = null

    private lateinit var binding: FragmentReuseBinding

    private val kaiYanViewModel: KaiYanViewModel by viewModels()

    //适配器
    private val pagingAdapter by lazy {
        when (type) {
            Constant.RECOMMEND_FRAGMENT -> RecommendPagingAdapter()//推荐
            Constant.DAILY_FRAGMENT -> DailyPagingAdapter()//日报
            Constant.SQUARE_FRAGMENT -> SquarePagingAdapter()//广场
            Constant.CATEGORY_FRAGMENT -> CategoryPagingAdapter()//分类
            Constant.REPLY_FRAGMENT -> ReplyPagingAdapter()//评论
            Constant.NOTIFICATION_FRAGMENT -> NotificationPagingAdapter()//通知
            Constant.HOT_WEEKLY_FRAGMENT -> HotPagingAdapter()//周排行
            Constant.HOT_MONTHLY_FRAGMENT -> HotPagingAdapter()//月排行
            Constant.HOT_HISTORICAL_FRAGMENT -> HotPagingAdapter()//总排行
            Constant.CATEGORY_RECOMMEND_FRAGMENT -> CategoryRecommendPagingAdapter()//分类推荐
            Constant.CATEGORY_DYNAMICS_FRAGMENT -> CategoryDynamicsPagingAdapter()//分类动态
            Constant.COLLECT_FRAGMENT -> CollectPagingAdapter()//分类动态
            else -> throw IllegalArgumentException("Unknown type: $type")
        }
    }

    // 添加是否为首次启动的标记
    private var isFirstLaunch = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            type = it.getString(Constant.FRAGMENT_TYPE)
            tagId = it.getString(Constant.TAG_ID)
            videoInfoData = it.getSerializable(Constant.VIDEO_INFO_DATA) as VideoInfoData?
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentReuseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initData()

        initEvent()

    }

    private fun initEvent() {
        //重新加载
        binding.constraintLayoutRetry.setOnClickListener {
            pagingAdapter.refresh()
        }
    }

    private fun initData() {

        binding.swipeRefreshLayout.setColorSchemeColors(
            ContextCompat.getColor(requireContext(), R.color.custom_color),
            ContextCompat.getColor(requireContext(), R.color.red)
        )
        //刷新
        binding.swipeRefreshLayout.setOnRefreshListener {
            pagingAdapter.refresh()
        }

        //添加适配器及尾部布局
        binding.recyclerView.adapter = pagingAdapter.withLoadStateFooter(PagingFooterAdapter {
            pagingAdapter.retry()
        })

        //刷新状态监听
        pagingAdapter.addLoadStateListener { loadState ->
            when (loadState.refresh) {

                is LoadState.Loading -> {
                    binding.swipeRefreshLayout.isVisible = true
                    binding.swipeRefreshLayout.isRefreshing = true
                    binding.constraintLayoutRetry.isVisible = false
                }

                is LoadState.NotLoading -> {
                    binding.swipeRefreshLayout.isVisible = true
                    binding.swipeRefreshLayout.isRefreshing = false
                    binding.constraintLayoutRetry.isVisible = false
                }

                is LoadState.Error -> {
                    binding.swipeRefreshLayout.isVisible = false
                    binding.swipeRefreshLayout.isRefreshing = false
                    binding.constraintLayoutRetry.isVisible = true
                }
            }
        }

        //绑定适配器并初始化数据，延迟一点防止页面滑动卡顿
        lifecycleScope.launch {
            delay(200)
            bindToAdapter()
        }
    }

    private fun bindToAdapter() {
        when (type) {
            /**
             * 推荐页
             */
            Constant.RECOMMEND_FRAGMENT -> {
                val recommendPagingAdapter = pagingAdapter as RecommendPagingAdapter
                binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
                bindAdapterToPaging(
                    kaiYanViewModel.getRecommendPagingData(), recommendPagingAdapter
                )
                //跳转到播放页面
                recommendPagingAdapter.setOnItemClickListener(object :
                    RecommendPagingAdapter.OnItemClickListener {
                    override fun onItemClick(videoInfoData: VideoInfoData, view: View) {
                        vibrate()
                        PlayVideoActivity.start(requireActivity(), videoInfoData)
                    }
                })
            }
            /**
             * 日报页
             */
            Constant.DAILY_FRAGMENT -> {
                val dailyPagingAdapter = pagingAdapter as DailyPagingAdapter
                binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
                bindAdapterToPaging(
                    kaiYanViewModel.getDailyPagingData(), dailyPagingAdapter
                )
                //跳转到播放页面
                dailyPagingAdapter.setOnItemClickListener(object :
                    DailyPagingAdapter.OnItemClickListener {
                    override fun onItemClick(videoInfoData: VideoInfoData, view: View) {
                        vibrate()
                        PlayVideoActivity.start(requireActivity(), videoInfoData)
                    }
                })
            }
            /**
             * 广场页
             */
            Constant.SQUARE_FRAGMENT -> {
                val squarePagingAdapter = pagingAdapter as SquarePagingAdapter
                binding.recyclerView.layoutManager =
                    StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
                bindAdapterToPaging(
                    kaiYanViewModel.getSquarePagingData(), squarePagingAdapter
                )

                //跳转到卡片详情页面
                var lastClickTime = 0L
                squarePagingAdapter.setOnItemClickListener(object :
                    SquarePagingAdapter.OnItemClickListener {
                    override fun onItemClick(squareData: SquareData, view: View) {
                        val now = System.currentTimeMillis()
                        if (now - lastClickTime < 1000) {
                            return
                        }
                        lastClickTime = now
                        vibrate()
                        val sourceData = SourceData(
                            squareData.picUrls,
                            SourceData.Consumption(
                                squareData.consumption.collectionCount,
                                squareData.consumption.shareCount,
                                squareData.consumption.replyCount,
                            ),
                            squareData.playUrl,
                            squareData.coverUrl,
                            if (squareData.type == 2) "video" else "photo",
                            squareData.date,
                            squareData.title,
                            squareData.headerUrl,
                            squareData.nickname
                        )
                        val squareDataList = squarePagingAdapter.getCurrentData()
                        val sourceDataList = mutableListOf<SourceData>()
                        squareDataList.forEach {
                            sourceDataList.add(
                                SourceData(
                                    it.picUrls,
                                    SourceData.Consumption(
                                        squareData.consumption.collectionCount,
                                        squareData.consumption.shareCount,
                                        squareData.consumption.replyCount,
                                    ),
                                    it.playUrl,
                                    it.coverUrl,
                                    if (it.type == 2) "video" else "photo",
                                    it.date,
                                    it.title,
                                    it.headerUrl,
                                    it.nickname
                                )
                            )
                        }
                        PhotoVideoActivity.start(requireActivity(), sourceData, sourceDataList)
                    }
                })
            }

            /**
             * 分类页
             */
            Constant.CATEGORY_FRAGMENT -> {
                val categoryPagingAdapter = pagingAdapter as CategoryPagingAdapter
                binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
                bindAdapterToPaging(
                    kaiYanViewModel.getCategoryPagingData(), categoryPagingAdapter
                )

                //跳转到卡片详情页面
                var lastClickTime = 0L
                categoryPagingAdapter.setOnItemClickListener(object :
                    CategoryPagingAdapter.OnItemClickListener {
                    override fun onItemClick(categoryData: CategoryData, view: View) {
                        val now = System.currentTimeMillis()
                        if (now - lastClickTime < 1000) {
                            return
                        }
                        lastClickTime = now
                        vibrate()
                        CategoryActivity.start(requireActivity(), categoryData)
                    }
                })
            }

            /**
             * 评论页
             */
            Constant.REPLY_FRAGMENT -> {
                val replyPagingAdapter = pagingAdapter as ReplyPagingAdapter
                binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
                bindAdapterToPaging(
                    kaiYanViewModel.getReplyPagingData(videoInfoData?.id.toString()),
                    replyPagingAdapter
                )
            }
            /**
             * 通知页
             */
            Constant.NOTIFICATION_FRAGMENT -> {
                val notificationPagingAdapter = pagingAdapter as NotificationPagingAdapter
                binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
                bindAdapterToPaging(
                    kaiYanViewModel.getNotificationPagingData(), notificationPagingAdapter
                )
                //跳转到通知详情界面
                notificationPagingAdapter.setOnItemClickListener(object :
                    NotificationPagingAdapter.OnItemClickListener {
                    override fun onItemClick(notificationData: NotificationData, view: View) {
                        vibrate()
                        val actionUrl = notificationData.actionUrl
                        // 解析actionUrl获取真正的网页链接
                        val urlStartIndex = actionUrl.indexOf("url=") + 4
                        val urlEndIndex = actionUrl.length
                        val encodedUrl = actionUrl.substring(urlStartIndex, urlEndIndex)
                        val decodedUrl = java.net.URLDecoder.decode(encodedUrl, "UTF-8")
                        // 使用Intent打开内置浏览器
                        WebViewActivity.start(requireActivity(), decodedUrl)
                    }
                })
            }

            /**
             * 周排行
             */
            Constant.HOT_WEEKLY_FRAGMENT -> {
                val hotPagingAdapter = pagingAdapter as HotPagingAdapter
                binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
                bindAdapterToPaging(
                    kaiYanViewModel.getHotPagingData("weekly"), hotPagingAdapter
                )
                //跳转到播放界面
                hotPagingAdapter.setOnItemClickListener(object :
                    HotPagingAdapter.OnItemClickListener {
                    override fun onItemClick(videoInfoData: VideoInfoData, view: View) {
                        vibrate()
                        PlayVideoActivity.start(requireActivity(), videoInfoData)
                    }
                })
            }
            /**
             * 月排行
             */
            Constant.HOT_MONTHLY_FRAGMENT -> {
                val hotPagingAdapter = pagingAdapter as HotPagingAdapter
                binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
                bindAdapterToPaging(
                    kaiYanViewModel.getHotPagingData("monthly"), hotPagingAdapter
                )
                //跳转到播放界面
                hotPagingAdapter.setOnItemClickListener(object :
                    HotPagingAdapter.OnItemClickListener {
                    override fun onItemClick(videoInfoData: VideoInfoData, view: View) {
                        vibrate()
                        PlayVideoActivity.start(requireActivity(), videoInfoData)
                    }
                })
            }
            /**
             * 总排行
             */
            Constant.HOT_HISTORICAL_FRAGMENT -> {
                val hotPagingAdapter = pagingAdapter as HotPagingAdapter
                binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
                bindAdapterToPaging(
                    kaiYanViewModel.getHotPagingData("historical"), hotPagingAdapter
                )
                //跳转到播放界面
                hotPagingAdapter.setOnItemClickListener(object :
                    HotPagingAdapter.OnItemClickListener {
                    override fun onItemClick(videoInfoData: VideoInfoData, view: View) {
                        vibrate()
                        PlayVideoActivity.start(requireActivity(), videoInfoData)
                    }
                })
            }
            /**
             * 分类推荐页
             */
            Constant.CATEGORY_RECOMMEND_FRAGMENT -> {
                val categoryRecommendPagingAdapter = pagingAdapter as CategoryRecommendPagingAdapter
                binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
                bindAdapterToPaging(
                    kaiYanViewModel.getCategoryRecommendPagingData(tagId.toString()),
                    categoryRecommendPagingAdapter
                )
                //跳转到播放页面
                categoryRecommendPagingAdapter.setOnItemClickListener(object :
                    CategoryRecommendPagingAdapter.OnItemClickListener {
                    override fun onItemClick(videoInfoData: VideoInfoData, view: View) {
                        vibrate()
                        PlayVideoActivity.start(requireActivity(), videoInfoData)
                    }
                })
            }

            /**
             * 分类动态页
             */
            Constant.CATEGORY_DYNAMICS_FRAGMENT -> {
                val categoryDynamicsPagingAdapter = pagingAdapter as CategoryDynamicsPagingAdapter
                binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
                bindAdapterToPaging(
                    kaiYanViewModel.getCategoryDynamicsPagingData(tagId.toString()),
                    categoryDynamicsPagingAdapter
                )
                //跳转到播放图片视频页面
                var lastClickTime: Long = 0
                categoryDynamicsPagingAdapter.setOnItemClickListener(object :
                    CategoryDynamicsPagingAdapter.OnItemClickListener {
                    override fun onItemClick(
                        categoryDynamicsData: CategoryDynamicsData, view: View
                    ) {
                        val now = System.currentTimeMillis()
                        if (now - lastClickTime < 1000) {
                            return
                        }
                        lastClickTime = now
                        vibrate()
                        val sourceData = SourceData(
                            categoryDynamicsData.picUrls,
                            SourceData.Consumption(
                                categoryDynamicsData.consumption.collectionCount,
                                categoryDynamicsData.consumption.shareCount,
                                categoryDynamicsData.consumption.replyCount,
                            ),
                            categoryDynamicsData.playUrl,
                            categoryDynamicsData.coverUrl,
                            if (categoryDynamicsData.type == 2) "video" else "photo",
                            categoryDynamicsData.releaseDate,
                            categoryDynamicsData.description,
                            categoryDynamicsData.headerUrl,
                            categoryDynamicsData.nickname
                        )
                        val categoryDynamicsDataList =
                            categoryDynamicsPagingAdapter.getCurrentData()
                        val sourceDataList = mutableListOf<SourceData>()
                        categoryDynamicsDataList.forEach {
                            sourceDataList.add(
                                SourceData(
                                    it.picUrls,
                                    SourceData.Consumption(
                                        it.consumption.collectionCount,
                                        it.consumption.shareCount,
                                        it.consumption.replyCount,
                                    ),
                                    it.playUrl,
                                    it.coverUrl,
                                    if (it.type == 2) "video" else "photo",
                                    it.releaseDate,
                                    it.description,
                                    it.headerUrl,
                                    it.nickname
                                )
                            )
                        }
                        PhotoVideoActivity.start(requireActivity(), sourceData, sourceDataList)
                    }
                })
            }
            /**
             * 收藏页
             */
            Constant.COLLECT_FRAGMENT -> {
                val collectPagingAdapter = pagingAdapter as CollectPagingAdapter
                binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
                bindAdapterToPaging(
                    kaiYanViewModel.getVideosPagingData(), collectPagingAdapter
                )
                //跳转到播放页面
                collectPagingAdapter.setOnItemClickListener(object :
                    CollectPagingAdapter.OnItemClickListener {
                    override fun onItemClick(videoInfoData: VideoInfoData, view: View) {
                        vibrate()
                        PlayVideoActivity.start(requireActivity(), videoInfoData)
                    }
                })
                //手动取消收藏
                collectPagingAdapter.setUnCollectClickListener(object :
                    CollectPagingAdapter.OnUnCollectClickListener {
                    override fun onUnCollectClick(videoInfoData: VideoInfoData) {
                        val customDialog = CustomDialog(
                            requireContext(),
                            resources.getString(R.string.sure_un_collected),
                            resources.getString(R.string.cancel),
                            resources.getString(R.string.confirm)
                        )
                        customDialog.show()
                        customDialog.setOnDoubleButtonClickListener(object :
                            CustomDialog.OnDoubleButtonClickListener {
                            override fun onLeftButtonClick() {}

                            override fun onRightButtonClick() {
                                lifecycleScope.launch {
                                    kaiYanViewModel.deleteVideo(videoInfoData)
                                }
                            }
                        })
                    }
                })
            }
        }
    }

    fun refresh() {
        pagingAdapter.refresh()
    }

    fun toTop() {
        binding.recyclerView.scrollToPosition(0)
    }

    override fun onResume() {
        super.onResume()
        if (!isFirstLaunch) {
            binding.swipeRefreshLayout.isRefreshing = false
            binding.swipeRefreshLayout.clearAnimation()
        }
    }

    override fun onPause() {
        super.onPause()
        isFirstLaunch = false
    }

}