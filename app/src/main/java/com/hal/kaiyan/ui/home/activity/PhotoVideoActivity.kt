package com.hal.kaiyan.ui.home.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.view.View
import androidx.core.view.children
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.gyf.immersionbar.ImmersionBar
import com.hal.kaiyan.R
import com.hal.kaiyan.adapter.PhotoVideoPagerAdapter
import com.hal.kaiyan.databinding.ActivityPhotoVideoBinding
import com.hal.kaiyan.entity.SourceData
import com.hal.kaiyan.ui.base.BaseActivity
import com.hal.kaiyan.ui.base.Constant
import com.hal.kaiyan.utils.IntentData
import com.hal.kaiyan.utils.viewBinding
import com.hal.kaiyan.view.MyVideoPlayer
import com.shuyu.gsyvideoplayer.GSYVideoADManager
import com.shuyu.gsyvideoplayer.GSYVideoManager
import com.shuyu.gsyvideoplayer.video.base.GSYBaseVideoPlayer

/**
 * ...
 * @author LeiHao
 * @date 2023/12/12
 * @description 播放视频和查看照片的界面
 */

class PhotoVideoActivity : BaseActivity() {

    companion object {
        @JvmStatic
        fun start(activity: Activity, sourceData: SourceData, sourceDataList: List<SourceData>) {
            val intent = Intent(activity, PhotoVideoActivity::class.java).apply {
                IntentData.setData(Constant.SOURCE_DATA, sourceData)
                IntentData.setData(Constant.SOURCE_DATA_LIST, sourceDataList)
            }
            activity.startActivity(intent)
            activity.overridePendingTransition(R.anim.anl_push_bottom_in, R.anim.anl_push_up_out)
        }
    }

    private val binding: ActivityPhotoVideoBinding by viewBinding()

    private lateinit var context: Context

    //正在显示的数据
    private var sourceData: SourceData? = null

    //整个数据
    private var sourceDataList: List<SourceData>? = null

    //当前所处的位置
    private var itemPosition: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        context = this

        ImmersionBar.with(this).transparentStatusBar().statusBarDarkFont(false).init()

        initData()

    }

    /**
     * 获取当前位置
     */
    private fun getCurrentItemPosition(): Int {
        sourceDataList?.forEachIndexed { index, item ->
            if (sourceData == item) {
                itemPosition = index
                return@forEachIndexed
            }
        }
        return itemPosition
    }

    private fun initData() {
        //拿到单个数据
        sourceData = IntentData.getData(Constant.SOURCE_DATA) as SourceData?
        //拿到所有数据
        sourceDataList = IntentData.getData(Constant.SOURCE_DATA_LIST) as List<SourceData>?

        itemPosition = getCurrentItemPosition()

        sourceDataList?.let {
            binding.viewPager2.adapter = PhotoVideoPagerAdapter(this, it)
            binding.viewPager2.orientation = ViewPager2.ORIENTATION_VERTICAL
            binding.viewPager2.offscreenPageLimit = 1
            binding.viewPager2.registerOnPageChangeCallback(
                AutoPlayPageChangeListener(
                    binding.viewPager2, itemPosition
                )
            )
        }
        //来到当前这个item的位置
        binding.viewPager2.setCurrentItem(itemPosition, false)
    }

    override fun onPause() {
        super.onPause()
        GSYVideoManager.onPause()
    }

    override fun onResume() {
        super.onResume()
        GSYVideoManager.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        GSYVideoADManager.releaseAllVideos()
    }

    override fun onBackPressed() {
        if (GSYVideoManager.backFromWindowFull(this)) return
        super.onBackPressed()
    }

    /**
     * 自动播放视频ViewPager的适配器
     */
    class AutoPlayPageChangeListener(
        private val viewPager: ViewPager2, private var defaultPosition: Int
    ) : ViewPager2.OnPageChangeCallback() {

        private var isPageSelected = false

        override fun onPageScrolled(
            position: Int, positionOffset: Float, positionOffsetPixels: Int
        ) {
            super.onPageScrolled(position, positionOffset, positionOffsetPixels)
            // 如果初始位置等于当前位置且滚动偏移为0，则认为已经停止滚动
            if (defaultPosition == position && positionOffsetPixels == 0) {
                onPageScrollStateChanged(ViewPager2.SCROLL_STATE_IDLE)
                defaultPosition = -1 // 将默认位置重置为-1，避免重复处理
            }
        }

        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            isPageSelected = true // 标记页面已被选中
        }

        override fun onPageScrollStateChanged(state: Int) {
            super.onPageScrollStateChanged(state)
            // 当页面滚动状态为静止且页面已被选中时，开始播放视频
            if (state == ViewPager2.SCROLL_STATE_IDLE && isPageSelected) {
                playVideo()
                isPageSelected = false // 播放视频后重置页面选中状态
            }
        }

        private fun playVideo() {
            var myVideoPlayer: MyVideoPlayer? = null
            // 遍历ViewPager2中的子视图，查找可见的视频播放器
            viewPager.children.forEach { view ->
                if (view is RecyclerView) {
                    val layoutManager = view.layoutManager ?: return@forEach
                    val childCount = layoutManager.childCount

                    for (index in 0 until childCount) {
                        val player = layoutManager.getChildAt(index)
                            ?.findViewById<MyVideoPlayer>(R.id.videoPlayer)
                        val rect = Rect()
                        player?.getLocalVisibleRect(rect)
                        val height = player?.height ?: 0
                        val isPlayerVisible = rect.top == 0 && rect.bottom == height

                        // 判断播放器是否可见且需要播放视频
                        if (player != null && isPlayerVisible && player.visibility == View.VISIBLE && (player.currentPlayer.currentState == GSYBaseVideoPlayer.CURRENT_STATE_NORMAL || player.currentPlayer.currentState == GSYBaseVideoPlayer.CURRENT_STATE_ERROR)) {
                            myVideoPlayer = player // 找到需要播放的视频播放器
                        } else {
                            GSYVideoManager.releaseAllVideos() // 关闭其他视频播放器
                        }
                    }
                }
            }

            myVideoPlayer?.startPlayLogic() // 开始播放视频
        }
    }
}