package com.hal.kaiyan.view

import com.flyco.tablayout.listener.CustomTabEntity


/**
 * ...
 * @author LeiHao
 * @date 2023/12/14
 * @description 与CommonTabLayout搭配使用的实体类
 */

class TabEntity(
    private val title: String, //文字
    private val selectedIcon: Int = 0, //选中时的icon
    private val unSelectedIcon: Int = 0 //未选中时的icon
) : CustomTabEntity {

    override fun getTabTitle() = title

    override fun getTabSelectedIcon() = selectedIcon

    override fun getTabUnselectedIcon() = unSelectedIcon

}