package com.hal.kaiyan.entity

import java.io.Serializable


/**
 * ...
 * @author LeiHao
 * @date 2023/12/7
 * @description 自定义分类实体类 - 接口的图片不能用了，自己弄一张
 */

data class CategoryData(
    val id: Int,
    val name: String,
    val description: String,
    val tagId: Int,
    var coverUrl: String
) : Serializable
