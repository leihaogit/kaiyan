package com.hal.kaiyan.utils

import android.graphics.Rect
import android.view.View
import androidx.core.view.children
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.hal.kaiyan.R
import com.shuyu.gsyvideoplayer.GSYVideoManager
import com.shuyu.gsyvideoplayer.video.base.GSYBaseVideoPlayer


/**
 * ...
 * @author LeiHao
 * @date 2023/12/21
 * @description ViewPager 自动播放监听类
 */

class AutoPlayPageChangeListener(
    private val viewPager: ViewPager2, private var defaultPosition: Int
) : ViewPager2.OnPageChangeCallback() {

    private var isPageSelected = false

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
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
