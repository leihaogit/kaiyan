package com.hal.kaiyan.base

import android.app.Application
import android.content.Context
import androidx.annotation.CallSuper


/**
 * ...
 * @author LeiHao
 * @date 2023/11/28
 * @description 基础Application，提供全局的上下文、全局的初始化和全局状态管理
 */

open class BaseApp : Application() {

    companion object {
        lateinit var appContext: Context
            private set
    }

    @CallSuper
    override fun onCreate() {
        super.onCreate()
        appContext = this
    }

}