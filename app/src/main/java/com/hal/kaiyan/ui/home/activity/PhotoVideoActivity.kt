package com.hal.kaiyan.ui.home.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.gyf.immersionbar.ImmersionBar
import com.hal.kaiyan.R
import com.hal.kaiyan.adapter.PhotoVideoPagerAdapter
import com.hal.kaiyan.databinding.ActivityPhotoVideoBinding
import com.hal.kaiyan.entity.SourceData
import com.hal.kaiyan.ui.base.BaseActivity
import com.hal.kaiyan.ui.base.Constant
import com.hal.kaiyan.utils.AutoPlayPageChangeListener
import com.hal.kaiyan.utils.IntentDataUtils
import com.shuyu.gsyvideoplayer.GSYVideoADManager
import com.shuyu.gsyvideoplayer.GSYVideoManager

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
                IntentDataUtils.setData(Constant.SOURCE_DATA, sourceData)
                IntentDataUtils.setData(Constant.SOURCE_DATA_LIST, sourceDataList)
            }
            activity.startActivity(intent)
            activity.overridePendingTransition(R.anim.anl_push_bottom_in, R.anim.anl_push_up_out)
        }
    }

    private val binding by lazy { ActivityPhotoVideoBinding.inflate(layoutInflater) }

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
        sourceData = IntentDataUtils.getData(Constant.SOURCE_DATA) as SourceData?
        //拿到所有数据
        sourceDataList = IntentDataUtils.getData(Constant.SOURCE_DATA_LIST) as List<SourceData>?

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
}