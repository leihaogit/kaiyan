package com.hal.kaiyan.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.hal.kaiyan.base.BaseApp
import com.tencent.mmkv.MMKV
import java.io.File


/**
 * ...
 * @author LeiHao
 * @date 2023/11/27
 * @description APP操作工具类
 */

object AppUtils {

    /**
     * 保存字符串到 MMKV
     *
     * @param key   键
     * @param value 值
     */
    fun encodeMMKV(key: String?, value: String?) {
        MMKV.defaultMMKV().encode(key, value)
    }

    /**
     * 从默认的 MMKV 实例中获取字符串
     *
     * @param key          键
     * @param defaultValue 默认值
     * @return 获取到的字符串
     */
    fun decodeMMKV(key: String?, defaultValue: String?): String? {
        return MMKV.defaultMMKV().decodeString(key, defaultValue)
    }

    /**
     * 获取当前APP的版本号
     * @return 版本号 V1.0.0
     */
    fun getAppVersion(): String {
        return BaseApp.appContext.packageManager.getPackageInfo(
            BaseApp.appContext.packageName, 0
        ).versionName
    }

    /**
     * 分享应用的 APK 文件
     */
    fun shareApkFile(context: Context) {
        val apkFile = File(context.applicationContext.packageResourcePath)
        val intent = Intent()
        intent.action = Intent.ACTION_SEND
        intent.type = "*/*"
        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(apkFile))
        context.startActivity(intent)
    }

    /**
     * 清除应用缓存
     */
    fun clearAppCache() {
        val context = BaseApp.appContext
        // 清除内部缓存目录
        val cacheDir = context.cacheDir
        deleteDir(cacheDir)
    }

    /**
     * 计算目录大小
     *
     * @param dir 目录
     * @return 目录大小（单位：字节）
     */
    fun calculateDirSize(dir: File?): Long {
        var size: Long = 0

        if (dir != null && dir.exists() && dir.isDirectory) {
            val files = dir.listFiles()

            if (files != null) {
                for (file in files) {
                    if (file.isFile) {
                        size += file.length()
                    } else if (file.isDirectory) {
                        size += calculateDirSize(file)
                    }
                }
            }
        }
        return size
    }

    /**
     * 删除目录
     *
     * @param dir 目录
     * @return 是否删除成功
     */
    private fun deleteDir(dir: File?): Boolean {
        if (dir != null && dir.exists() && dir.isDirectory) {
            val files = dir.listFiles()

            if (files != null) {
                for (file in files) {
                    if (file.isFile) {
                        file.delete()
                    } else if (file.isDirectory) {
                        deleteDir(file)
                    }
                }
            }

            return dir.delete()
        }

        return false
    }
}