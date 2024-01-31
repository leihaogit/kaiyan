package com.hal.kaiyan.ui.base

import android.annotation.SuppressLint
import android.app.UiModeManager
import android.content.Context
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.PagingData
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gyf.immersionbar.ImmersionBar
import com.hal.kaiyan.R
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

/**
 * @author LeiHao
 * @date 2023/10/25
 * @description Activity的基类，包含强制竖屏、沉浸式状态栏+状态栏深色字体
 */

abstract class BaseActivity : AppCompatActivity() {

    @CallSuper
    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //强制竖屏
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        val uiModeManager = getSystemService(Context.UI_MODE_SERVICE) as UiModeManager

        val isDarkMode = when (uiModeManager.currentModeType) {
            //判断当前的日夜间模式
            Configuration.UI_MODE_TYPE_NORMAL -> {
                val nightModeFlags =
                    resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
                nightModeFlags == Configuration.UI_MODE_NIGHT_YES
            }

            else -> false
        }

        val statusBarColorRes = if (isDarkMode) R.color.black else R.color.white

        ImmersionBar.with(this)
            //状态栏颜色
            .statusBarColor(statusBarColorRes)
            //是否深色字体
            .statusBarDarkFont(!isDarkMode).init()

    }

    /**
     * 绑定分页数据到 RecyclerView 中的 Adapter
     */
    protected fun <T : Any, V : RecyclerView.ViewHolder, A : PagingDataAdapter<T, V>> bindAdapterToPaging(
        data: Flow<PagingData<T>>, pagingAdapter: A
    ) {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                data.collect {
                    pagingAdapter.submitData(it)
                }
            }
        }
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(0, R.anim.slide_out_top)
    }
}