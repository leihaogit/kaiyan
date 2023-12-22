package com.hal.kaiyan.entity

/**
 * ...
 * @author LeiHao
 * @date 2023/12/7
 * @description 广场实体类
 */
data class SquareEntity(
    val adIndex: Int,
    val `data`: Data,
    val id: Int,
    val tag: Any,
    val trackingData: Any,
    val type: String
) {
    data class Data(
        val adTrack: Any,
        val content: Content,
        val count: Int,
        val dataType: String,
        val footer: Any,
        val header: Any,
        val itemList: List<Item>
    ) {
        data class Content(
            val adIndex: Int,
            val `data`: Data,
            val id: Int,
            val tag: Any,
            val trackingData: Any,
            val type: String
        ) {
            data class Data(
                val addWatermark: Boolean,
                val area: String,
                val checkStatus: String,
                val city: String,
                val collected: Boolean,
                val consumption: Consumption,
                val cover: Cover,
                val createTime: Long,
                val dataType: String,
                val description: String,
                val duration: Int,
                val height: Int,
                val id: Int,
                val ifMock: Boolean,
                val latitude: String,
                val library: String,
                val longitude: String,
                val owner: Owner?,
                val playUrl: String,
                val playUrlWatermark: String,
                val privateMessageActionUrl: Any,
                val reallyCollected: Boolean,
                val recentOnceReply: RecentOnceReply,
                val releaseTime: Long,
                val resourceType: String,
                val selectedTime: Any,
                val source: String,
                val status: Any,
                val tags: Any,
                val title: String,
                val transId: Any,
                val type: String,
                val uid: String,
                val updateTime: Long,
                val url: String,
                val urls: List<String>,
                val urlsWithWatermark: List<String>,
                val validateResult: String,
                val validateStatus: String,
                val validateTaskId: String,
                val width: String
            ) {
                data class Consumption(
                    val collectionCount: Int,
                    val realCollectionCount: Int,
                    val replyCount: Int,
                    val shareCount: Int
                )

                data class Cover(
                    val blurred: Any,
                    val detail: String,
                    val feed: String,
                    val homepage: Any,
                    val sharing: Any
                )

                data class Owner(
                    val actionUrl: String,
                    val area: Any,
                    val avatar: String,
                    val birthday: Long,
                    val city: String,
                    val country: String,
                    val cover: String,
                    val description: String,
                    val expert: Boolean,
                    val followed: Boolean,
                    val gender: String,
                    val ifPgc: Boolean,
                    val job: String,
                    val library: String,
                    val limitVideoOpen: Boolean,
                    val nickname: String,
                    val registDate: Long,
                    val releaseDate: Long,
                    val uid: Int,
                    val university: String,
                    val userType: String
                )

                data class RecentOnceReply(
                    val actionUrl: String,
                    val contentType: Any,
                    val dataType: String,
                    val message: String,
                    val nickname: String
                )
            }
        }

        data class Item(
            val adIndex: Int,
            val `data`: Data,
            val id: Int,
            val tag: Any,
            val trackingData: Any,
            val type: String
        ) {
            data class Data(
                val actionUrl: String,
                val adTrack: List<Any>,
                val autoPlay: Boolean,
                val bgPicture: String,
                val dataType: String,
                val description: String,
                val header: Header,
                val id: Int,
                val image: String,
                val label: Label,
                val labelList: List<Label>,
                val shade: Boolean,
                val subTitle: String,
                val title: String
            ) {
                data class Header(
                    val actionUrl: Any,
                    val cover: Any,
                    val description: Any,
                    val font: Any,
                    val icon: Any,
                    val id: Int,
                    val label: Any,
                    val labelList: Any,
                    val rightText: Any,
                    val subTitle: Any,
                    val subTitleFont: Any,
                    val textAlign: String,
                    val title: Any
                )

                data class Label(
                    val card: String,
                    val detail: Any,
                    val text: String,
                    val actionUrl: Any
                )
            }
        }
    }
}