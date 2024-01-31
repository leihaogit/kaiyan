package com.hal.kaiyan.ui.home.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.hal.kaiyan.R
import com.hal.kaiyan.databinding.ActivityCollectBinding
import com.hal.kaiyan.ui.base.BaseActivity
import com.hal.kaiyan.ui.base.Constant
import com.hal.kaiyan.ui.base.ReusePagingFragment
import com.hal.kaiyan.utils.vibrate
import com.hal.kaiyan.utils.viewBinding
import com.hal.kaiyan.view.CustomDialog
import com.hal.kaiyan.viewmodel.KaiYanViewModel
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.launch

/**
 * ...
 * @author LeiHao
 * @date 2023/12/13
 * @description 本地收藏界面
 */

class CollectActivity : BaseActivity() {

    companion object {
        @JvmStatic
        fun start(activity: Activity) {
            val intent = Intent(activity, CollectActivity::class.java)
            activity.startActivity(intent)
            activity.overridePendingTransition(R.anim.anl_push_bottom_in, R.anim.anl_push_up_out)
        }
    }

    private val binding: ActivityCollectBinding by viewBinding()

    private lateinit var content: Context

    private val kaiYanViewModel: KaiYanViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        content = this

        initData()

        initEvent()
    }

    private fun initEvent() {
        //返回
        binding.imageViewBack.setOnClickListener {
            vibrate()
            finish()
        }

        //清空收藏
        binding.imageViewAllDelete.setOnClickListener {
            val customDialog = CustomDialog(
                content,
                resources.getString(R.string.sure_clear_all),
                resources.getString(R.string.cancel),
                resources.getString(R.string.confirm)
            )
            customDialog.show()
            customDialog.setOnDoubleButtonClickListener(object :
                CustomDialog.OnDoubleButtonClickListener {
                override fun onLeftButtonClick() {}

                override fun onRightButtonClick() {
                    lifecycleScope.launch {
                        kaiYanViewModel.deleteAllVideos()
                        Toasty.normal(
                            content, resources.getString(R.string.cleared), Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            })
        }

        kaiYanViewModel.hasVideoCount().observe(this) {
            binding.textViewTitle.text = resources.getString(R.string.collect, it)
            binding.imageViewAllDelete.isVisible = it != 0
        }
    }

    private fun initData() {
        binding.viewPager2.adapter = object : FragmentStateAdapter(this) {

            override fun getItemCount() = 1

            override fun createFragment(position: Int) =
                ReusePagingFragment.newInstance(
                    Constant.COLLECT_FRAGMENT,
                    null,
                    null
                )
        }
    }
}