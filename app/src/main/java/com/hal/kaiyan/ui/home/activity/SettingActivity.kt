package com.hal.kaiyan.ui.home.activity

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.StrictMode
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import com.hal.kaiyan.R
import com.hal.kaiyan.databinding.ActivitySettingBinding
import com.hal.kaiyan.ui.base.BaseActivity
import com.hal.kaiyan.ui.base.Constant
import com.hal.kaiyan.utils.AppUtils
import com.hal.kaiyan.utils.byteToMb
import com.hal.kaiyan.view.CustomDialog
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * ...
 * @author LeiHao
 * @date 2023/11/28
 * @description 设置界面
 */

class SettingActivity : BaseActivity() {

    companion object {
        @JvmStatic
        fun start(activity: Activity) {
            val intent = Intent(activity, SettingActivity::class.java)
            activity.startActivity(intent)
            activity.overridePendingTransition(R.anim.anl_push_bottom_in, R.anim.anl_push_up_out)
        }
    }

    private val binding by lazy {
        ActivitySettingBinding.inflate(layoutInflater)
    }

    private lateinit var context: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        context = this

        initData()

        initEvent()

    }

    private fun initData() {
        //解决不能使用file://分享apk的问题
        val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
        builder.detectFileUriExposure()

        //初始化日夜间模式选择状态
        when (AppUtils.decodeMMKV(
            Constant.KEY_NIGHT_MODE, Constant.FOLLOW_MODE
        )) {
            //日间模式
            Constant.DAY_MODE -> {
                binding.switchFollow.isChecked = false
                binding.switchNight.isChecked = false
                binding.constraintLayoutNight.visibility = View.VISIBLE
            }
            //夜间模式
            Constant.NIGHT_MODE -> {
                binding.switchFollow.isChecked = false
                binding.switchNight.isChecked = true
                binding.constraintLayoutNight.visibility = View.VISIBLE
            }
            //跟随系统模式
            Constant.FOLLOW_MODE -> {
                binding.switchFollow.isChecked = true
                binding.switchNight.isChecked = false
                binding.constraintLayoutNight.visibility = View.GONE
            }
        }

        //初始化自动全屏选择状态，没有选择过则默认手动
        when (AppUtils.decodeMMKV(
            Constant.KEY_AUTO_FULL_SCREEN, Constant.MANUAL_MODE
        )) {
            //自动模式
            Constant.AUTO_MODE -> {
                binding.switchAutoPlayVideo.isChecked = true
            }
            //手动模式
            else -> {
                binding.switchAutoPlayVideo.isChecked = false
            }
        }

        //初始化手动清理缓存的选择状态，没有选择过则默认手动
        when (AppUtils.decodeMMKV(
            Constant.KEY_AUTO_CLEAR_CACHE, Constant.MANUAL_MODE
        )) {
            //自动模式
            Constant.AUTO_MODE -> {
                binding.switchAutoClearCache.isChecked = true
            }
            //手动模式
            else -> {
                binding.switchAutoClearCache.isChecked = false
            }
        }

        //显示缓存大小
        val calculateDirSize =
            AppUtils.calculateDirSize(externalCacheDir) + AppUtils.calculateDirSize(cacheDir)
        binding.textViewCacheSize.text = calculateDirSize.byteToMb()
    }

    private fun initEvent() {

        //返回
        binding.imageViewBack.setOnClickListener {
            vibrate()
            finish()
        }

        //跟随系统
        binding.switchFollow.setOnClickListener {
            vibrate()
            //如果目前是跟随系统的选中状态
            if (binding.switchFollow.isChecked) {
                /**
                 * 设置为跟随系统
                 */
                AppUtils.encodeMMKV(
                    Constant.KEY_NIGHT_MODE, Constant.FOLLOW_MODE
                )
                // 设置为跟随系统
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                binding.switchNight.isChecked = false
                binding.constraintLayoutNight.visibility = View.GONE
            } else {
                /**
                 * 设置为非跟随系统，并打开日间模式
                 */
                AppUtils.encodeMMKV(
                    Constant.KEY_NIGHT_MODE, Constant.DAY_MODE
                )
                // 设置为非跟随系统，并打开日间模式
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                binding.constraintLayoutNight.visibility = View.VISIBLE
            }
        }

        //夜间模式
        binding.switchNight.setOnClickListener {
            vibrate()
            //如果目前是夜间模式选中状态
            if (binding.switchNight.isChecked) {
                /**
                 * 设置为夜间模式
                 */
                AppUtils.encodeMMKV(
                    Constant.KEY_NIGHT_MODE, Constant.NIGHT_MODE
                )
                // 设置为日间模式
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                /**
                 * 设置为日间模式
                 */
                AppUtils.encodeMMKV(
                    Constant.KEY_NIGHT_MODE, Constant.DAY_MODE
                )
                // 设置为日间模式
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        //清空缓存
        binding.constraintLayoutCache.setOnClickListener {
            vibrate()
            val customDialog = CustomDialog(
                context,
                resources.getString(R.string.sure_to_clear_cache),
                resources.getString(R.string.cancel),
                resources.getString(R.string.confirm)
            )

            customDialog.show()
            customDialog.setOnDoubleButtonClickListener(object :
                CustomDialog.OnDoubleButtonClickListener {
                override fun onLeftButtonClick() {
                    vibrate()
                }

                override fun onRightButtonClick() {
                    vibrate()
                    AppUtils.clearAppCache()
                    Toasty.success(context, resources.getString(R.string.success)).show()
                    //显示缓存大小
                    val calculateDirSize =
                        AppUtils.calculateDirSize(externalCacheDir) + AppUtils.calculateDirSize(
                            cacheDir
                        )
                    binding.textViewCacheSize.text = calculateDirSize.byteToMb()
                }
            })
        }

        //跳转QQ
        binding.textViewName.setOnClickListener {
            val qqNumber = "2070701720"
            try {
                val intent = Intent(
                    Intent.ACTION_VIEW, Uri.parse("mqqwpa://im/chat?chat_type=wpa&uin=$qqNumber")
                )
                startActivity(intent)
            } catch (_: ActivityNotFoundException) {
            }
        }

        //横屏自动全屏
        binding.switchAutoPlayVideo.setOnClickListener {
            vibrate()
            //如果目前选中了自动模式
            if (binding.switchAutoPlayVideo.isChecked) {
                /**
                 * 设置为自动全屏播放
                 */
                AppUtils.encodeMMKV(
                    Constant.KEY_AUTO_FULL_SCREEN, Constant.AUTO_MODE
                )
            } else {
                /**
                 * 设置为手动全屏播放
                 */
                AppUtils.encodeMMKV(
                    Constant.KEY_AUTO_FULL_SCREEN, Constant.MANUAL_MODE
                )
            }

        }

        //自动清理缓存
        binding.switchAutoClearCache.setOnClickListener {
            vibrate()
            //如果目前选中了自动模式
            if (binding.switchAutoClearCache.isChecked) {
                /**
                 * 设置为自动清理
                 */
                AppUtils.encodeMMKV(
                    Constant.KEY_AUTO_CLEAR_CACHE, Constant.AUTO_MODE
                )
            } else {
                /**
                 * 设置为手动清理
                 */
                AppUtils.encodeMMKV(
                    Constant.KEY_AUTO_CLEAR_CACHE, Constant.MANUAL_MODE
                )
            }
        }

        //分享apk
        binding.imageViewShare.setOnClickListener {
            vibrate()
            //防止双击
            binding.imageViewShare.isEnabled = false
            lifecycleScope.launch {
                delay(1000)
                binding.imageViewShare.isEnabled = true
            }
            AppUtils.shareApkFile(context)
        }

    }
}