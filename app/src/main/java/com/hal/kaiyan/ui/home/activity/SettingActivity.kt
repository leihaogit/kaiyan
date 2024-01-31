package com.hal.kaiyan.ui.home.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.StrictMode
import androidx.lifecycle.lifecycleScope
import com.hal.kaiyan.R
import com.hal.kaiyan.databinding.ActivitySettingBinding
import com.hal.kaiyan.ui.base.BaseActivity
import com.hal.kaiyan.ui.home.fragment.PreferenceSettingsFragment
import com.hal.kaiyan.utils.AppUtils
import com.hal.kaiyan.utils.vibrate
import com.hal.kaiyan.utils.viewBinding
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

    private val binding: ActivitySettingBinding by viewBinding()

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

        //放置 Fragment
        supportFragmentManager.beginTransaction()
            .replace(R.id.frameLayout, PreferenceSettingsFragment())
            .commit()

    }

    private fun initEvent() {

        //返回
        binding.imageViewBack.setOnClickListener {
            vibrate()
            finish()
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