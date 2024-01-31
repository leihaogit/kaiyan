package com.hal.kaiyan.ui.home.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.flyco.tablayout.listener.CustomTabEntity
import com.flyco.tablayout.listener.OnTabSelectListener
import com.gyf.immersionbar.ImmersionBar
import com.hal.kaiyan.R
import com.hal.kaiyan.databinding.ActivityCategoryBinding
import com.hal.kaiyan.entity.CategoryData
import com.hal.kaiyan.ui.base.BaseActivity
import com.hal.kaiyan.ui.base.Constant
import com.hal.kaiyan.ui.base.ReusePagingFragment
import com.hal.kaiyan.utils.viewBinding
import com.hal.kaiyan.view.TabEntity

class CategoryActivity : BaseActivity() {

    companion object {
        @JvmStatic
        fun start(activity: Activity, categoryData: CategoryData) {
            val intent = Intent(activity, CategoryActivity::class.java).apply {
                putExtra(Constant.CATEGORY_DATA, categoryData)
            }
            activity.startActivity(intent)
            activity.overridePendingTransition(R.anim.anl_push_bottom_in, R.anim.anl_push_up_out)
        }
    }

    private val binding: ActivityCategoryBinding by viewBinding()

    private lateinit var context: Context

    /**
     * 前一个界面传递过来的分类信息
     */
    private var categoryData: CategoryData? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        context = this

        ImmersionBar.with(this).transparentStatusBar().init()

        categoryData = intent.getSerializableExtra(Constant.CATEGORY_DATA) as CategoryData?

        initData()

    }

    private fun initData() {

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.baseline_arrow_back_24)
        binding.toolbar.setNavigationOnClickListener {
            vibrate()
            finish()
        }
        supportActionBar?.title = categoryData?.name

        //上方cover图片
        Glide.with(context).load(categoryData?.coverUrl).into(binding.imageViewCover)

        binding.viewPager2.adapter = object : FragmentStateAdapter(this) {

            override fun getItemCount() = 2

            override fun createFragment(position: Int): Fragment = when (position) {
                0 -> ReusePagingFragment.newInstance(
                    Constant.CATEGORY_RECOMMEND_FRAGMENT,
                    categoryData?.tagId.toString(),
                    null
                )

                else -> ReusePagingFragment.newInstance(
                    Constant.CATEGORY_DYNAMICS_FRAGMENT,
                    categoryData?.tagId.toString(),
                    null
                )
            }

        }

        binding.tabLayout.setTabData(ArrayList<CustomTabEntity>().apply {
            add(TabEntity(resources.getString(R.string.recommend)))
            add(TabEntity(resources.getString(R.string.dynamics)))
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
}