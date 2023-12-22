package com.hal.kaiyan.ui.base


/**
 * ...
 * @author LeiHao
 * @date 2023/12/11
 * @description 常量类 - 所有常量统一在这里进行管理
 */

object Constant {

    /**
     * ========================================================================
     * 序列化通用常量
     */

    //Fragment类型
    const val FRAGMENT_TYPE = "type"

    //类型id，用于分类界面
    const val TAG_ID = "tagId"

    //视频数据
    const val VIDEO_INFO_DATA = "videoInfoData"

    //分类数据
    const val CATEGORY_DATA = "categoryData"

    //资源数据
    const val SOURCE_DATA = "sourceData"

    //当前全部资源数据
    const val SOURCE_DATA_LIST = "sourceDataList"

    //解析出来的网址
    const val DECODE_URL = "decodeUrl"

    /**
     * ========================================================================
     * Fragment的唯一标识常量
     */

    //分页Fragment标识
    const val RECOMMEND_FRAGMENT = "1" //推荐
    const val DAILY_FRAGMENT = "2" //日报
    const val SQUARE_FRAGMENT = "3" //广场
    const val CATEGORY_FRAGMENT = "4" //分类
    const val REPLY_FRAGMENT = "5" //评论
    const val NOTIFICATION_FRAGMENT = "6" //消息通知
    const val HOT_WEEKLY_FRAGMENT = "7" // 周榜
    const val HOT_MONTHLY_FRAGMENT = "8" // 月榜
    const val HOT_HISTORICAL_FRAGMENT = "9" // 周榜
    const val CATEGORY_RECOMMEND_FRAGMENT = "10" // 分类推荐
    const val CATEGORY_DYNAMICS_FRAGMENT = "11" // 分类动态
    const val COLLECT_FRAGMENT = "12" // 收藏页

    /**
     * ========================================================================
     * MMKV 的键值
     */

    /**
     * key
     */
    //日夜间模式的key
    const val KEY_NIGHT_MODE = "key_night_mode"

    //自动全屏模式的key
    const val KEY_AUTO_FULL_SCREEN = "key_auto_full_screen"

    //自动清理缓存的key
    const val KEY_AUTO_CLEAR_CACHE = "key_auto_clear_cache"

    /**
     * value
     */
    //日间模式
    const val DAY_MODE = "day"

    //夜间模式
    const val NIGHT_MODE = "night"

    //跟随系统
    const val FOLLOW_MODE = "follow"

    //自动模式
    const val AUTO_MODE = "auto"

    //手动模式
    const val MANUAL_MODE = "manual"


}