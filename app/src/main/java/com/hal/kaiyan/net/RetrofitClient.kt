package com.hal.kaiyan.net

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


/**
 * ...
 * @author LeiHao
 * @date 2023/10/27
 * @description Retrofit 工具类
 */

object RetrofitClient {

    //baseUrl 必须以/结尾，否则会报错
    private const val BASE_URL = "http://baobab.kaiyanapp.com"

    // 创建自定义的 OkHttpClient 实例
    private val customOkHttpClient =
        OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS) // 设置连接超时时间为 10 秒
            .readTimeout(30, TimeUnit.SECONDS) // 设置读取超时时间为 30 秒
            .writeTimeout(30, TimeUnit.SECONDS) // 设置写入超时时间为 30 秒
            .build()


    // 创建 Retrofit 实例
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder().baseUrl(BASE_URL) // 设置基础 URL
            .client(customOkHttpClient) // 使用自定义的 OkHttpClient
            .addConverterFactory(GsonConverterFactory.create()) // 添加 Gson 转换器工厂
            .build()
    }

    /**
     * 创建指定类型的服务接口实例
     *
     * @param serviceClass 服务接口的类对象
     * @return 服务接口实例
     */
    fun <T> getService(serviceClass: Class<T>): T {
        return retrofit.create(serviceClass)
    }

}