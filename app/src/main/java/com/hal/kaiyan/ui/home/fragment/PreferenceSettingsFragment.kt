package com.hal.kaiyan.ui.home.fragment

import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import com.hal.kaiyan.R
import com.hal.kaiyan.ui.base.Constant
import com.hal.kaiyan.utils.AppUtils
import com.hal.kaiyan.utils.byteToMb
import com.hal.kaiyan.utils.vibrate
import com.hal.kaiyan.view.CustomDialog
import es.dmoral.toasty.Toasty

/**
 * ...
 * @author LeiHao
 * @date 2024/1/31
 * @description 偏好设置页面
 */

class PreferenceSettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings_preferences, rootKey)
        //初始化偏好设置
        initPreferences()
    }

    private fun initPreferences() {
        /**
         * 深色设置
         */
        val switchFollow: SwitchPreferenceCompat = findPreference("switch_follow")!!
        val switchNight: SwitchPreferenceCompat = findPreference("switch_night")!!
        //初始化日夜间模式选择状态
        when (AppUtils.decodeMMKV(
            Constant.KEY_NIGHT_MODE, Constant.FOLLOW_MODE
        )) {
            //日间模式
            Constant.DAY_MODE -> {
                switchFollow.isChecked = false
                switchNight.isChecked = false
                switchNight.isEnabled = true
            }
            //夜间模式
            Constant.NIGHT_MODE -> {
                switchFollow.isChecked = false
                switchNight.isChecked = true
                switchNight.isEnabled = true
            }
            //跟随系统模式
            Constant.FOLLOW_MODE -> {
                switchFollow.isChecked = true
                switchNight.isChecked = false
                switchNight.isEnabled = false
            }
        }
        /**
         * 点击跟随系统
         */
        switchFollow.let {
            it.setOnPreferenceChangeListener { _, newValue ->
                val isChecked = newValue as Boolean
                vibrate()
                if (isChecked) {
                    //设置为跟随系统
                    AppUtils.encodeMMKV(
                        Constant.KEY_NIGHT_MODE, Constant.FOLLOW_MODE
                    )
                    // 设置为跟随系统
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                    switchNight.isChecked = false
                    switchNight.isEnabled = false
                } else {
                    //设置为非跟随系统，并打开日间模式
                    AppUtils.encodeMMKV(
                        Constant.KEY_NIGHT_MODE, Constant.DAY_MODE
                    )
                    // 设置为非跟随系统，并打开日间模式
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    switchNight.isEnabled = true
                }
                true
            }
        }

        /**
         * 点击夜间模式
         */
        switchNight.let {
            it.setOnPreferenceChangeListener { _, newValue ->
                val isChecked = newValue as Boolean
                vibrate()
                if (isChecked) {
                    //设置为夜间模式
                    AppUtils.encodeMMKV(
                        Constant.KEY_NIGHT_MODE, Constant.NIGHT_MODE
                    )
                    // 设置为日间模式
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                } else {
                    //设置为日间模式
                    AppUtils.encodeMMKV(
                        Constant.KEY_NIGHT_MODE, Constant.DAY_MODE
                    )
                    // 设置为日间模式
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
                true
            }
        }

        /**
         * 横屏自动全屏
         */
        val automaticFullScreenPlay: SwitchPreferenceCompat =
            findPreference("automatic_full_screen_play")!!
        //初始化自动全屏选择状态，没有选择过则默认手动
        when (AppUtils.decodeMMKV(
            Constant.KEY_AUTO_FULL_SCREEN, Constant.MANUAL_MODE
        )) {
            //自动模式
            Constant.AUTO_MODE -> {
                automaticFullScreenPlay.isChecked = true
            }
            //手动模式
            else -> {
                automaticFullScreenPlay.isChecked = false
            }
        }
        automaticFullScreenPlay.let {
            it.setOnPreferenceChangeListener { _, newValue ->
                val isChecked = newValue as Boolean
                vibrate()
                if (isChecked) {
                    //设置为自动全屏播放
                    AppUtils.encodeMMKV(
                        Constant.KEY_AUTO_FULL_SCREEN, Constant.AUTO_MODE
                    )
                } else {
                    //设置为手动全屏播放
                    AppUtils.encodeMMKV(
                        Constant.KEY_AUTO_FULL_SCREEN, Constant.MANUAL_MODE
                    )
                }
                true
            }
        }

        /**
         * 清空缓存
         */
        val clearCache: Preference = findPreference("clear_cache")!!
        clearCache.let {
            //初始化缓存大小
            val calculateDirSize =
                AppUtils.calculateDirSize(requireContext().externalCacheDir) + AppUtils.calculateDirSize(
                    requireContext().cacheDir
                )
            it.summary = calculateDirSize.byteToMb()
            //点击清空
            it.setOnPreferenceClickListener {
                vibrate()
                val customDialog = CustomDialog(
                    requireContext(),
                    resources.getString(R.string.sure_to_clear_cache),
                    resources.getString(R.string.cancel),
                    resources.getString(R.string.confirm)
                )
                customDialog.show()
                customDialog.setOnDoubleButtonClickListener(object :
                    CustomDialog.OnDoubleButtonClickListener {
                    override fun onLeftButtonClick() {
                        vibrate()
                    }

                    override fun onRightButtonClick() {
                        vibrate()
                        AppUtils.clearAppCache()
                        clearCache.summary = 0L.byteToMb()
                        Toasty.success(requireContext(), resources.getString(R.string.success))
                            .show()
                    }
                })
                true
            }
        }

        /**
         * 自动清空缓存
         */
        val autoClearCache: SwitchPreferenceCompat = findPreference("auto_clear_cache")!!
        //初始化自动清理缓存的选择状态，没有选择过则默认手动
        when (AppUtils.decodeMMKV(
            Constant.KEY_AUTO_CLEAR_CACHE, Constant.MANUAL_MODE
        )) {
            //自动模式
            Constant.AUTO_MODE -> {
                autoClearCache.isChecked = true
            }
            //手动模式
            else -> {
                autoClearCache.isChecked = false
            }
        }
        //点击自动清理缓存按钮
        autoClearCache.let {
            it.setOnPreferenceChangeListener { _, newValue ->
                val isChecked = newValue as Boolean
                vibrate()
                if (isChecked) {
                    //设置为自动清理
                    AppUtils.encodeMMKV(
                        Constant.KEY_AUTO_CLEAR_CACHE, Constant.AUTO_MODE
                    )
                } else {
                    //设置为手动清理
                    AppUtils.encodeMMKV(
                        Constant.KEY_AUTO_CLEAR_CACHE, Constant.MANUAL_MODE
                    )
                }
                true
            }
        }
    }
}
