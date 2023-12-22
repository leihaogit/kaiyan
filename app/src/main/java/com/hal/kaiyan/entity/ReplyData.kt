package com.hal.kaiyan.entity

/**
 * ...
 * @author LeiHao
 * @date 2023/12/9
 * @description 自定义评论数据
 */

data class ReplyData(
    val id: Long, //id
    val message: String, //评论内容
    val createTime: Long, //评论的时间
    val likeCount: Int, //点赞数
    val nickname: String, //昵称
    val avatar: String, //头像链接
)