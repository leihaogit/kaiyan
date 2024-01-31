package com.hal.kaiyan.ui.home.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebViewClient
import com.hal.kaiyan.R
import com.hal.kaiyan.databinding.ActivityWebViewBinding
import com.hal.kaiyan.ui.base.BaseActivity
import com.hal.kaiyan.ui.base.Constant
import com.hal.kaiyan.utils.viewBinding

class WebViewActivity : BaseActivity() {

    companion object {
        @JvmStatic
        fun start(activity: Activity, decodeUrl: String) {
            val intent = Intent(activity, WebViewActivity::class.java).apply {
                putExtra(Constant.DECODE_URL, decodeUrl)
            }
            activity.startActivity(intent)
            activity.overridePendingTransition(R.anim.anl_push_bottom_in, R.anim.anl_push_up_out)
        }
    }

    private val binding: ActivityWebViewBinding by viewBinding()

    private var url: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        url = intent.getStringExtra(Constant.DECODE_URL)

        initData()

        initEvent()

    }

    private fun initData() {
        if (url == null) {
            finish()
        } else {
            val webSettings: WebSettings = binding.webView.settings
            webSettings.javaScriptEnabled = true // 启用JavaScript支持
            webSettings.setSupportZoom(true) // 支持缩放
            binding.webView.webViewClient = WebViewClient() // 在WebView中打开链接，而不是使用默认的浏览器
            binding.webView.webChromeClient = WebChromeClient() // 显示网页加载进度
            binding.webView.loadUrl(url!!)
        }
    }

    private fun initEvent() {
        binding.imageViewBack.setOnClickListener {
            finish()
        }
    }

    override fun onBackPressed() {
        if (binding.webView.canGoBack()) {
            binding.webView.goBack() // 返回上一个网页
        } else {
            super.onBackPressed() // 退出当前Activity
        }
    }
}