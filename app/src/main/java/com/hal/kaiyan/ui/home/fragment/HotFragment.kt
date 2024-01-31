package com.hal.kaiyan.ui.home.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.flyco.tablayout.listener.CustomTabEntity
import com.flyco.tablayout.listener.OnTabSelectListener
import com.hal.kaiyan.R
import com.hal.kaiyan.databinding.FragmentHotBinding
import com.hal.kaiyan.ui.base.BaseFragment
import com.hal.kaiyan.ui.base.Constant
import com.hal.kaiyan.ui.base.ReusePagingFragment
import com.hal.kaiyan.ui.home.activity.SettingActivity
import com.hal.kaiyan.utils.vibrate
import com.hal.kaiyan.utils.viewBinding
import com.hal.kaiyan.view.TabEntity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * @author LeiHao
 * @date 2023/10/25
 * @description 热门页面
 */

class HotFragment : BaseFragment() {

    private val binding: FragmentHotBinding by viewBinding()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initData()

        initEvent()

    }


    private fun initEvent() {
        //去设置界面
        binding.imageViewSetting.setOnClickListener {
            vibrate()
            //防止双击
            binding.imageViewSetting.isEnabled = false
            lifecycleScope.launch {
                delay(1000)
                binding.imageViewSetting.isEnabled = true
            }
            SettingActivity.start(requireActivity())
        }
    }

    private fun initData() {

        binding.viewPager2.adapter = object : FragmentStateAdapter(this) {

            override fun getItemCount() = 3

            override fun createFragment(position: Int): Fragment {
                return when (position) {
                    0 -> ReusePagingFragment.newInstance(
                        Constant.HOT_WEEKLY_FRAGMENT,
                        null,
                        null
                    )

                    1 -> ReusePagingFragment.newInstance(
                        Constant.HOT_MONTHLY_FRAGMENT,
                        null,
                        null
                    )

                    else -> ReusePagingFragment.newInstance(
                        Constant.HOT_HISTORICAL_FRAGMENT,
                        null,
                        null
                    )
                }
            }
        }

        binding.tabLayout.setTabData(ArrayList<CustomTabEntity>().apply {
            add(TabEntity(resources.getString(R.string.weekly)))
            add(TabEntity(resources.getString(R.string.monthly)))
            add(TabEntity(resources.getString(R.string.historical)))

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
    }

    fun refresh() {
        binding.appBarLayout.setExpanded(true)
        val currentFragment =
            childFragmentManager.findFragmentByTag("f${binding.viewPager2.currentItem}") as? ReusePagingFragment
        currentFragment?.refresh()
    }

    fun toTop() {
        binding.appBarLayout.setExpanded(true)
        val currentFragment =
            childFragmentManager.findFragmentByTag("f${binding.viewPager2.currentItem}") as? ReusePagingFragment
        currentFragment?.toTop()
    }

}