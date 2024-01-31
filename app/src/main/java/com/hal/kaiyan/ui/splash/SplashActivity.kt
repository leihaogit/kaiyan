package com.hal.kaiyan.ui.splash

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.gyf.immersionbar.BarHide
import com.gyf.immersionbar.ImmersionBar
import com.hal.kaiyan.R
import com.hal.kaiyan.databinding.ActivitySplashBinding
import com.hal.kaiyan.ui.home.activity.HomeActivity
import com.hal.kaiyan.utils.AppUtils
import com.hal.kaiyan.utils.viewBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


/**
 * @author LeiHao
 * @date 2023/10/24
 * @description 启动欢迎页
 */

class SplashActivity : AppCompatActivity() {

    private val binding: ActivitySplashBinding by viewBinding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //隐藏状态栏和导航栏
        ImmersionBar.with(this).hideBar(BarHide.FLAG_HIDE_BAR).init()

        //设置版本号
        binding.textViewVersion.text =
            resources.getString(R.string.version, AppUtils.getAppVersion())

        //500毫秒后跳转至主页
        lifecycleScope.launch {
            delay(500)
            HomeActivity.start(this@SplashActivity)
            finish()
        }
    }
}