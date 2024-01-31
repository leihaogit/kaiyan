package com.hal.kaiyan.ui.home.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.hal.kaiyan.R
import com.hal.kaiyan.databinding.ActivityHomeBinding
import com.hal.kaiyan.ui.base.BaseActivity
import com.hal.kaiyan.ui.base.Constant
import com.hal.kaiyan.ui.home.fragment.FindFragment
import com.hal.kaiyan.ui.home.fragment.HomeFragment
import com.hal.kaiyan.ui.home.fragment.HotFragment
import com.hal.kaiyan.utils.AppUtils
import com.hal.kaiyan.utils.vibrate
import com.hal.kaiyan.utils.viewBinding
import es.dmoral.toasty.Toasty

class HomeActivity : BaseActivity() {

    companion object {
        @JvmStatic
        fun start(activity: Activity) {
            val intent = Intent(activity, HomeActivity::class.java)
            activity.startActivity(intent)
            activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }
    }

    private val binding: ActivityHomeBinding by viewBinding()

    //双击退出
    private var backPressedOnce = false

    private lateinit var context: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        context = this

        initData()

    }

    private fun initData() {

        binding.viewPager2.adapter = object : FragmentStateAdapter(this) {

            override fun getItemCount() = 3

            override fun createFragment(position: Int): Fragment {
                return when (position) {
                    0 -> HomeFragment()
                    1 -> FindFragment()
                    else -> HotFragment()
                }
            }
        }
        //不可滑动
        binding.viewPager2.isUserInputEnabled = false

        var lastClickTime: Long = 0
        val doubleClickDelay: Long = 300 // 规定的双击间隔时间

        // 点击下方item进行切换
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            val currentTime = System.currentTimeMillis()
            val isDoubleClick =
                item.itemId == binding.bottomNavigationView.selectedItemId && currentTime - lastClickTime < doubleClickDelay
            lastClickTime = currentTime

            when (item.itemId) {
                R.id.navigation_home -> {
                    vibrate()
                    if (binding.viewPager2.currentItem != 0) {
                        binding.viewPager2.currentItem = 0
                    } else {
                        val currentFragment =
                            supportFragmentManager.findFragmentByTag("f0") as? HomeFragment
                        if (isDoubleClick) {
                            currentFragment?.refresh()
                        } else {
                            currentFragment?.toTop()
                        }
                    }
                }

                R.id.navigation_find -> {
                    vibrate()
                    if (binding.viewPager2.currentItem != 1) {
                        binding.viewPager2.currentItem = 1
                    } else {
                        val currentFragment =
                            supportFragmentManager.findFragmentByTag("f1") as? FindFragment
                        if (isDoubleClick) {
                            currentFragment?.refresh()
                        } else {
                            currentFragment?.toTop()
                        }
                    }
                }

                R.id.navigation_hot -> {
                    vibrate()
                    if (binding.viewPager2.currentItem != 2) {
                        binding.viewPager2.currentItem = 2
                    } else {
                        val currentFragment =
                            supportFragmentManager.findFragmentByTag("f2") as? HotFragment
                        if (isDoubleClick) {
                            currentFragment?.refresh()
                        } else {
                            currentFragment?.toTop()
                        }
                    }
                }

                else -> {
                    return@setOnItemSelectedListener false
                }
            }

            return@setOnItemSelectedListener true
        }
    }

    override fun onBackPressed() {
        if (backPressedOnce) {
            super.onBackPressed()
        } else {
            backPressedOnce = true
            Toasty.normal(context, resources.getString(R.string.double_quit), Toasty.LENGTH_SHORT)
                .show()
            // 延迟2秒后重置标志位
            Handler(mainLooper).postDelayed({ backPressedOnce = false }, 2000)
        }
    }

    override fun finish() {
        super.finish()
        //如果设置了自动清理缓存，就将缓存清理掉
        when (AppUtils.decodeMMKV(Constant.KEY_AUTO_CLEAR_CACHE, Constant.MANUAL_MODE)) {
            Constant.AUTO_MODE -> {
                AppUtils.clearAppCache()
            }
        }
    }

}
