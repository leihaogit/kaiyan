package com.hal.kaiyan.entity


/**
 * ...
 * @author LeiHao
 * @date 2023/12/5
 * @description 自定义二级实体类itemList
 */


data class ItemList(
    val `data`: Data, val type: String
) {
    data class Data(
        val itemList: List<ItemList>,//基本数据
        val dataType: String,   // 数据类型
        val id: Int,            // ID
        val title: String,      // 标题
        val description: String,   // 描述
        val text: String,       // 文本内容
        val header: Header,     // 头部信息
        val content: Content,   // 内容信息
        val tags: List<Tag>,    // 标签列表
        val consumption: Consumption,  // 信息
        val resourceType: String,  // 资源类型
        val provider: Provider,  // 提供者信息
        val category: String,   // 类别
        val author: Author?,     // 作者信息
        val cover: Cover,       // 封面信息
        val playUrl: String,    // 播放地址
        val duration: Int,      // 持续时间
        val webUrl: WebUrl,     // 网页链接信息
        val releaseTime: Long,    // 发布时间
        val playInfo: List<PlayInfo>,  // 播放信息列表
        val type: String,        // 类型
        val titlePgc: String,    // PGC标题
        val descriptionPgc: String,   // PGC描述
        val date: Long,          // 日期
        val descriptionEditor: String   // 编辑描述
    ) {

        data class Header(
            val id: Int,            // ID
            val title: String,      // 标题
            val actionUrl: String,  // 动作链接
            val icon: String,       // 图标
            val description: String,   // 描述
            val time: Long          // 时间
        )

        data class Content(
            val type: String,       // 类型
            val `data`: Data        // 数据
        )

        data class Tag(
            val id: Int,            // ID
            val name: String,       // 名称
            val actionUrl: String,  // 动作链接
            val bgPicture: String,  // 背景图片
            val headerImage: String,   // 头部图片
            val tagRecType: String, // 标签推荐类型
            val communityIndex: Int // 社区索引
        )

        data class Consumption(
            val collectionCount: Int,   // 收藏数
            val realCollectionCount: Int,   // 实际收藏数
            val replyCount: Int,       // 回复数
            val shareCount: Int        // 分享数
        )


        data class Provider(
            val alias: String,   // 别名
            val icon: String,    // 图标
            val name: String     // 名称
        )

        data class Author(
            val id: Int,            // ID
            val icon: String,       // 图标
            val name: String,       // 名称
            val description: String,    // 描述
            val latestReleaseTime: Long,    // 最新发布时间
            val link: String,       // 链接
            val videoNum: Int,      // 视频数量
            val follow: Follow,     // 关注信息
            val shield: Shield      // 屏蔽信息
        ) {
            data class Follow(
                val followed: Boolean,   // 是否已关注
                val itemId: Int,         // 项ID
                val itemType: String     // 项类型
            )

            data class Shield(
                val itemId: Int,         // 项ID
                val itemType: String,    // 项类型
                val shielded: Boolean    // 是否已屏蔽
            )
        }


        data class Cover(
            val blurred: String,    // 模糊封面
            val detail: String,     // 详细封面
            val feed: String,       // 动态封面
            val homepage: Any,      // 主页封面
            val sharing: Any        // 分享封面
        )

        data class WebUrl(
            val forWeibo: String,   // 微博链接
            val raw: String         // 原始链接
        )

        data class PlayInfo(
            val height: Int,        // 高度
            val name: String,       // 名称
            val type: String,       // 类型
            val url: String,        // URL
            val urlList: List<Url>, // URL列表
            val width: Int          // 宽度
        ) {
            data class Url(
                val name: String,   // 名称
                val size: Int,      // 大小
                val url: String     // URL
            )
        }

    }
}
