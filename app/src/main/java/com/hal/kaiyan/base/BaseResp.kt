package com.hal.kaiyan.base

import com.google.gson.annotations.SerializedName
import com.hal.kaiyan.net.DataState


/**
 * ...
 * @author LeiHao
 * @date 2023/11/30
 * @description 用于Retrofit的基础响应体
 * 开眼接口统一返回格式，有用的只有itemList和nextPageUrl：
 * {
 *     "itemList": [], //数据列表
 *     "nextPageUrl": "", //下一页的url
 * }
 * dataState 用来实时跟踪网络状态，不是接口返回的
 * error 是捕获到的异常
 */
class BaseResp<T>(
    @SerializedName("itemList") val itemList: T? = null,
    @SerializedName("nextPageUrl") val nextPageUrl: String? = null,
    @SerializedName("dataState") var dataState: DataState? = null,
    @SerializedName("error") var error: Throwable? = null,
)

/**
 * ...
 * @author LeiHao
 * @date 2023/12/9
 * @description 专门用于统计的响应体，因为字段名称不一样，要单独处理一下
 */
class BaseNotificationResp<T>(
    @SerializedName("messageList") val messageList: T? = null,
    @SerializedName("nextPageUrl") val nextPageUrl: String? = null,
    @SerializedName("dataState") var dataState: DataState? = null,
    @SerializedName("error") var error: Throwable? = null,
)