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
import com.hal.kaiyan.App
import com.hal.kaiyan.R
import com.hal.kaiyan.databinding.FragmentHomeBinding
import com.hal.kaiyan.ui.base.BaseFragment
import com.hal.kaiyan.ui.base.Constant
import com.hal.kaiyan.ui.base.ReusePagingFragment
import com.hal.kaiyan.ui.home.activity.CollectActivity
import com.hal.kaiyan.view.TabEntity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * @author LeiHao
 * @date 2023/10/25
 * @description 首页页面
 */

class HomeFragment : BaseFragment() {

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initData()

        initEvent()

    }

    private fun initEvent() {
        //去收藏页面
        binding.imageViewLike.setOnClickListener {
            App.vibrate()
            //防止双击
            binding.imageViewLike.isEnabled = false
            lifecycleScope.launch {
                delay(1000)
                binding.imageViewLike.isEnabled = true
            }
            CollectActivity.start(requireActivity())
        }
    }

    private fun initData() {

        binding.viewPager2.adapter = object : FragmentStateAdapter(this) {

            override fun getItemCount() = 2

            override fun createFragment(position: Int): Fragment {
                return when (position) {
                    0 -> ReusePagingFragment.newInstance(
                        Constant.RECOMMEND_FRAGMENT,
                        null,
                        null
                    )

                    else -> ReusePagingFragment.newInstance(
                        Constant.DAILY_FRAGMENT,
                        null,
                        null
                    )
                }
            }
        }

        binding.tabLayout.setTabData(ArrayList<CustomTabEntity>().apply
        {
            add(TabEntity(resources.getString(R.string.recommend)))
            add(TabEntity(resources.getString(R.string.daily)))
        })

        binding.tabLayout.setOnTabSelectListener(
            object : OnTabSelectListener {

                override fun onTabSelect(position: Int) {
                    binding.viewPager2.currentItem = position
                }

                override fun onTabReselect(position: Int) {
                }
            })


        binding.viewPager2.registerOnPageChangeCallback(
            object : ViewPager2.OnPageChangeCallback() {
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