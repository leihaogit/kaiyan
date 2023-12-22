package com.hal.kaiyan

import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.appcompat.app.AppCompatDelegate
import com.hal.kaiyan.base.BaseApp
import com.hal.kaiyan.ui.base.Constant
import com.hal.kaiyan.utils.AppUtils
import com.tencent.mmkv.MMKV

/**
 * ...
 * @author LeiHao
 * @date 2023/11/28
 * @description 自定义的 Application 类，用于进行应用程序的全局配置和初始化工作。
 */

class App : BaseApp() {

    companion object {

        //震动
        fun vibrate() {
            val vibrator: Vibrator = appContext.getSystemService(Vibrator::class.java)
            if (vibrator.hasVibrator()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    val vibrationEffect =
                        VibrationEffect.createPredefined(VibrationEffect.EFFECT_TICK)
                    vibrator.vibrate(vibrationEffect)
                } else {
                    val amplitude = VibrationEffect.DEFAULT_AMPLITUDE
                    val vibrationEffect = VibrationEffect.createOneShot(5, amplitude)
                    vibrator.vibrate(vibrationEffect)
                }
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        MMKV.initialize(this)
        setNightMode()
    }

    //初始设置日间还是夜间模式还是跟随系统
    private fun setNightMode() {
        when (AppUtils.decodeMMKV(
            Constant.KEY_NIGHT_MODE, Constant.FOLLOW_MODE
        )) {
            Constant.NIGHT_MODE -> {
                // 设置夜间模式的主题和样式
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }

            Constant.DAY_MODE -> {
                // 设置日间模式的主题和样式
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }

            Constant.FOLLOW_MODE -> {
                // 设置跟随系统的主题和样式
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            }

            else -> {
                //为空，设置为跟随系统，保存跟随系统标志
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                AppUtils.encodeMMKV(
                    Constant.KEY_NIGHT_MODE, Constant.FOLLOW_MODE
                )
            }
        }
    }
}