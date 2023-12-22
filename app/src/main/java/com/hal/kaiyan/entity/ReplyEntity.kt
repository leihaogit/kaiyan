package com.hal.kaiyan.entity

/**
 * ...
 * @author LeiHao
 * @date 2023/12/9
 * @description 自定义评论数据
 */

data class ReplyEntity(
    val type: String, // 是评论还是文字
    val data: Data, // 数据
) {
    data class Data(
        val dataType: String, //评论类型
        val id: Long, //id
        val message: String, //评论内容
        val createTime: Long, //评论的时间
        val user: User?,//评论人的信息
        val likeCount: Int //点赞数
    ) {
        data class User(
            val uid: Int, //uid
            val nickname: String, //昵称
            val avatar: String //头像链接
        )
    }

}