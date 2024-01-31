package com.hal.kaiyan.ui.home.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.hal.kaiyan.R
import com.hal.kaiyan.databinding.ActivityNotificationBinding
import com.hal.kaiyan.ui.base.BaseActivity
import com.hal.kaiyan.ui.base.Constant
import com.hal.kaiyan.ui.base.ReusePagingFragment
import com.hal.kaiyan.utils.vibrate
import com.hal.kaiyan.utils.viewBinding


/**
 * ...
 * @author LeiHao
 * @date 2023/12/9
 * @description 通知页面
 */

class NotificationActivity : BaseActivity() {

    companion object {
        @JvmStatic
        fun start(activity: Activity) {
            val intent = Intent(activity, NotificationActivity::class.java)
            activity.startActivity(intent)
            activity.overridePendingTransition(R.anim.anl_push_bottom_in, R.anim.anl_push_up_out)
        }
    }

    private val binding: ActivityNotificationBinding by viewBinding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initData()

        initEvent()

    }

    private fun initData() {
        binding.viewPager2.adapter = object : FragmentStateAdapter(this) {

            override fun getItemCount() = 1

            override fun createFragment(position: Int): Fragment {
                return ReusePagingFragment.newInstance(Constant.NOTIFICATION_FRAGMENT, null, null)
            }
        }
    }

    private fun initEvent() {

        //返回
        binding.imageViewBack.setOnClickListener {
            vibrate()
            finish()
        }

    }
}