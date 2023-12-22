package com.hal.kaiyan.net


/**
 * ...
 * @author LeiHao
 * @date 2023/12/6
 * @description 数据的响应状态
 */

enum class DataState {
    LOADING,//加载中
    SUCCESS,//加载成功
    COMPLETED,//加载完成
    ERROR//加载失败
}