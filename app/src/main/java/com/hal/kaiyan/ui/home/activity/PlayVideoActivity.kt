package com.hal.kaiyan.ui.home.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.flyco.tablayout.listener.CustomTabEntity
import com.flyco.tablayout.listener.OnTabSelectListener
import com.gyf.immersionbar.ImmersionBar
import com.hal.kaiyan.R
import com.hal.kaiyan.databinding.ActivityPlayVideoBinding
import com.hal.kaiyan.entity.VideoInfoData
import com.hal.kaiyan.ui.base.BaseActivity
import com.hal.kaiyan.ui.base.Constant
import com.hal.kaiyan.ui.base.ReusePagingFragment
import com.hal.kaiyan.ui.home.fragment.RelatedFragment
import com.hal.kaiyan.utils.AppUtils
import com.hal.kaiyan.view.TabEntity
import com.shuyu.gsyvideoplayer.GSYVideoADManager
import com.shuyu.gsyvideoplayer.GSYVideoManager
import com.shuyu.gsyvideoplayer.utils.OrientationUtils
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * ...
 * @author LeiHao
 * @date 2023/12/6
 * @description 视频播放页面，注意去配置文件中加：
 * android:configChanges="keyboard|keyboardHidden|orientation|screenSize|screenLayout|smallestScreenSize|uiMode"
 */

class PlayVideoActivity : BaseActivity() {

    companion object {
        @JvmStatic
        fun start(activity: Activity, videoInfoData: VideoInfoData) {
            val intent = Intent(activity, PlayVideoActivity::class.java).apply {
                putExtra(Constant.VIDEO_INFO_DATA, videoInfoData)
            }
            activity.startActivity(intent)
            activity.overridePendingTransition(R.anim.anl_push_bottom_in, R.anim.anl_push_up_out)
        }
    }

    private val binding by lazy { ActivityPlayVideoBinding.inflate(layoutInflater) }

    private lateinit var context: Context

    private var orientationUtils: OrientationUtils? = null

    /**
     * 前一个界面传递过来的视频信息
     */
    private var videoInfoData: VideoInfoData? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        context = this

        ImmersionBar.with(this).statusBarColor(R.color.black).statusBarDarkFont(false).init()

        videoInfoData = intent.getSerializableExtra(Constant.VIDEO_INFO_DATA) as VideoInfoData?

        initData()

    }

    private fun initData() {

        Glide.with(context).load(videoInfoData?.coverBlurred).into(binding.imageViewBlurredBg)

        orientationUtils = OrientationUtils(this, binding.videoPlayer)

        binding.viewPager2.adapter = object : FragmentStateAdapter(this) {

            override fun getItemCount() = 2

            override fun createFragment(position: Int): Fragment = when (position) {
                0 -> RelatedFragment.newInstance(videoInfoData)

                else -> ReusePagingFragment.newInstance(
                    Constant.REPLY_FRAGMENT, null, videoInfoData
                )
            }
        }

        binding.tabLayout.setTabData(ArrayList<CustomTabEntity>().apply {
            add(TabEntity(resources.getString(R.string.introduction)))
            add(
                TabEntity(
                    resources.getString(
                        R.string.reply, videoInfoData?.consumption?.replyCount
                    )
                )
            )
        })

        binding.tabLayout.setOnTabSelectListener(object : OnTabSelectListener {

            override fun onTabSelect(position: Int) {
                binding.viewPager2.currentItem = position
            }

            override fun onTabReselect(position: Int) {
            }
        })


        binding.viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.tabLayout.currentTab = position
            }
        })

        //是否自动全屏
        orientationUtils!!.isRotateWithSystem = !AppUtils.decodeMMKV(
            Constant.KEY_AUTO_FULL_SCREEN, Constant.MANUAL_MODE
        ).equals(Constant.AUTO_MODE)

        //配置播放信息
        initPlayer()
        //开始播放，延迟一点防止卡顿
        lifecycleScope.launch {
            delay(200)
            binding.videoPlayer.startPlayLogic()
        }
    }


    //去某个地方，这里主要是去评论区
    fun toPosition(position: Int) {
        binding.viewPager2.currentItem = position
    }

    private fun initPlayer() {
        //配置播放信息
        binding.videoPlayer.apply {
            //设置全屏按键功能,这是使用的是选择屏幕，而不是全屏
            fullscreenButton.setOnClickListener { orientationUtils?.resolveByClick() }
            //关闭旋转动画
            isShowFullAnimation = false
            //音频焦点冲突时是否释放
            isReleaseWhenLossAudio = false
            //封面
            val imageView = ImageView(context)
            imageView.scaleType = ImageView.ScaleType.CENTER_CROP
            Glide.with(context).load(videoInfoData?.coverUrl).into(imageView)
            thumbImageView = imageView
            //是否需要全屏锁定屏幕功能
            isNeedLockFull = true
            //是否可以滑动调整
            setIsTouchWiget(true)
            //设置播放URL
            setUp(videoInfoData?.playUrl, false, videoInfoData?.title)
            //返回键
            backButton.setOnClickListener {
                onBackPressed()
            }
        }
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
        orientationUtils?.releaseListener()
    }

    override fun onBackPressed() {
        orientationUtils?.backToProtVideo()
        if (GSYVideoManager.backFromWindowFull(this)) return
        super.onBackPressed()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        binding.videoPlayer.onConfigurationChanged(this, newConfig, orientationUtils, true, true)
    }

    fun refresh(videoInfoData: VideoInfoData) {
        this.videoInfoData = videoInfoData
        initData()
    }
}