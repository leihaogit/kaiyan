package com.hal.kaiyan.view

import android.app.Dialog
import android.content.Context
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.hal.kaiyan.R

/**
 * @author LeiHao
 * @date 2023/9/28
 * @description 自定义弹窗
 */

class CustomDialog : Dialog {

    //返回键是否能关闭弹窗
    private var isBackButtonEnabled = true

    private var singleButtonClickListener: OnSingleButtonClickListener? = null
    private var doubleButtonClickListener: OnDoubleButtonClickListener? = null

    /**
     * 设置单按钮点击监听
     */
    fun setOnSingleButtonClickListener(listener: OnSingleButtonClickListener) {
        this.singleButtonClickListener = listener
    }

    /**
     * 设置双按钮点击监听
     */
    fun setOnDoubleButtonClickListener(listener: OnDoubleButtonClickListener) {
        this.doubleButtonClickListener = listener
    }

    //单按钮回调
    interface OnSingleButtonClickListener {
        /**
         * 点击按钮进行的动作
         */
        fun onSingleButtonClick()
    }

    //双按钮回调
    interface OnDoubleButtonClickListener {
        /**
         * 点击左边按钮进行的动作
         */
        fun onLeftButtonClick()

        /**
         * 点击右边按钮进行的动作
         */
        fun onRightButtonClick()
    }

    constructor(
        context: Context,
        content: String,
        singleButtonText: String,
    ) : super(context, R.style.CustomDialogTheme) {
        // 设置布局
        setContentView(R.layout.custom_dialog_layout)
        //默认点击弹窗外不能关闭弹窗
        setCanceledOnTouchOutside(false)
        initializeView(content, singleButtonText)
    }

    constructor(
        context: Context, content: String, leftButtonText: String, rightButtonText: String
    ) : super(context, R.style.CustomDialogTheme) {
        // 设置布局
        setContentView(R.layout.custom_dialog_layout)
        //默认点击弹窗外不能关闭弹窗
        setCanceledOnTouchOutside(false)
        initializeView(content, leftButtonText, rightButtonText)
    }

    /**
     * 是否能用返回键关闭弹窗，true-可以 false-不可以，默认是 true - 可以
     */
    fun setBackButtonEnabled(enabled: Boolean) {
        isBackButtonEnabled = enabled
    }

    // 开启返回键
    override fun onBackPressed() {
        if (isBackButtonEnabled) {
            dismiss()
        }
    }

    private fun initializeView(
        content: String, singleButtonText: String
    ) {

        val singleButton = findViewById<Button>(R.id.single_button)
        val leftButton = findViewById<Button>(R.id.left_button)
        val rightButton = findViewById<Button>(R.id.right_button)
        val divider = findViewById<View>(R.id.divider_line)
        val contentTextView = findViewById<TextView>(R.id.content)

        singleButton.visibility = View.VISIBLE
        leftButton.visibility = View.GONE
        rightButton.visibility = View.GONE
        divider.visibility = View.GONE

        contentTextView.text = content
        singleButton.text = singleButtonText

        singleButton.setOnClickListener {
            singleButtonClickListener?.onSingleButtonClick()
            dismiss()
        }

    }

    private fun initializeView(
        content: String, leftButtonText: String, rightButtonText: String
    ) {

        val singleButton = findViewById<Button>(R.id.single_button)
        val leftButton = findViewById<Button>(R.id.left_button)
        val rightButton = findViewById<Button>(R.id.right_button)
        val divider = findViewById<View>(R.id.divider_line)
        val contentTextView = findViewById<TextView>(R.id.content)

        singleButton.visibility = View.GONE
        leftButton.visibility = View.VISIBLE
        rightButton.visibility = View.VISIBLE
        divider.visibility = View.VISIBLE

        contentTextView.text = content
        leftButton.text = leftButtonText
        rightButton.text = rightButtonText

        leftButton.setOnClickListener {
            doubleButtonClickListener?.onLeftButtonClick()
            dismiss()
        }

        rightButton.setOnClickListener {
            doubleButtonClickListener?.onRightButtonClick()
            dismiss()
        }
    }


}