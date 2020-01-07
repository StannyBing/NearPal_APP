package com.stanny.nearpal.base

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.stanny.nearpal.app.MyApplication

/**
 * Created by Xiangb on 2017/12/1.
 * 功能：带字体的文本
 */
class TypeTextView @JvmOverloads constructor(context: Context?, attrs: AttributeSet? = null) : AppCompatTextView(context, attrs) {
    init {
        typeface = MyApplication.typeFace
    }
}