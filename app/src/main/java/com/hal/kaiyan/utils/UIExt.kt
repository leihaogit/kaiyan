package com.hal.kaiyan.utils

import android.app.Activity
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.LayoutInflater
import androidx.annotation.MainThread
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding
import com.hal.kaiyan.base.BaseApp

/**
 * 用于 Activity 快捷构建对应 ViewBinding
 */
@MainThread
inline fun <reified VB : ViewBinding> Activity.viewBinding() = object : Lazy<VB> {
    private var binding: VB? = null
    override val value: VB
        get() = binding ?: VB::class.java.getMethod(
            "inflate",
            LayoutInflater::class.java,
        ).invoke(null, layoutInflater).let {
            if (it is VB) {
                binding = it
                it
            } else {
                throw ClassCastException()
            }
        }

    override fun isInitialized(): Boolean = binding != null
}

/**
 * 用于 Fragment 快捷构建对应 ViewBinding
 */
@MainThread
inline fun <reified VB : ViewBinding> Fragment.viewBinding() = object : Lazy<VB> {
    private var binding: VB? = null
    override val value: VB
        get() = binding ?: VB::class.java.getMethod(
            "inflate",
            LayoutInflater::class.java,
        ).invoke(null, requireActivity().layoutInflater).let {
            if (it is VB) {
                viewLifecycleOwner.lifecycle.addObserver(object : LifecycleEventObserver {
                    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                        if (event == Lifecycle.Event.ON_DESTROY) {
                            binding = null
                        }
                    }
                })
                binding = it
                it
            } else {
                throw ClassCastException()
            }
        }

    override fun isInitialized(): Boolean = binding != null
}

/**
 * 震动扩展
 */
fun Activity.vibrate() {
    val vibrator: Vibrator = BaseApp.appContext.getSystemService(Vibrator::class.java)
    if (vibrator.hasVibrator()) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val vibrationEffect = VibrationEffect.createPredefined(VibrationEffect.EFFECT_TICK)
            vibrator.vibrate(vibrationEffect)
        } else {
            val amplitude = VibrationEffect.DEFAULT_AMPLITUDE
            val vibrationEffect = VibrationEffect.createOneShot(5, amplitude)
            vibrator.vibrate(vibrationEffect)
        }
    }
}

fun Fragment.vibrate() {
    val vibrator: Vibrator = BaseApp.appContext.getSystemService(Vibrator::class.java)
    if (vibrator.hasVibrator()) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val vibrationEffect = VibrationEffect.createPredefined(VibrationEffect.EFFECT_TICK)
            vibrator.vibrate(vibrationEffect)
        } else {
            val amplitude = VibrationEffect.DEFAULT_AMPLITUDE
            val vibrationEffect = VibrationEffect.createOneShot(5, amplitude)
            vibrator.vibrate(vibrationEffect)
        }
    }
}