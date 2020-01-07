package com.stanny.nearpal.module.letter.func.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.KeyEvent
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import com.stanny.nearpal.R
import com.stanny.nearpal.app.MyApplication

/**
 * Created by Xiangb on 2017/12/1.
 * 功能：带字体的文本
 */
@SuppressLint("NewApi")
class LetterEditView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
    AppCompatEditText(context, attrs) {

    private var isFirstEdit = true

    init {
        typeface = MyApplication.typeFace
        background = null
        setTextColor(ContextCompat.getColor(context, R.color.gray))
        letterSpacing = 0.3f
        setLineSpacing(20f, 1f)
        setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(R.dimen.text_biger_size))

        gravity = Gravity.TOP

        highlightColor = ContextCompat.getColor(context, R.color.aliceblue)

//        setTextIsSelectable(true)//设置文字可以选中

        val typeArray = context.obtainStyledAttributes(attrs, R.styleable.LetterEditView)
        val writeable = typeArray.getBoolean(R.styleable.LetterEditView_writeable, false)
//        if (!writeable) {
//            keyListener = null
//        }
        isCursorVisible = writeable
        isFocusable = writeable
        isFocusableInTouchMode = writeable
        typeArray.recycle()

        //监听换行
        setOnEditorActionListener { v, actionId, event ->
            if (event.keyCode == KeyEvent.KEYCODE_ENTER) {
                postDelayed({
                    val editIndex = selectionStart
                    text?.insert(editIndex, "　　")//设置换行缩进
                }, 10)
            }
            return@setOnEditorActionListener false
        }
//        addTextChangedListener(object : TextWatcher {
//            override fun afterTextChanged(s: Editable?) {
//            }
//
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//                if (isFirstEdit && text.toString().isEmpty()) {
//                    text?.append("　　")
//                    isFirstEdit = false
//                }
//            }
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                if (isFirstEdit) {
//                    text?.insert(0, "　　")
//                    isFirstEdit = false
//                }
//            }
//
//        })
        addTextChangedListener {
            if (writeable) {
                if (isFirstEdit) {
                    isFirstEdit = false
                    text?.insert(0, "　　")
                }
                if (text.toString().isEmpty()) {
                    isFirstEdit = true
                }
            }
        }
    }

    override fun setText(text: CharSequence?, type: BufferType?) {
        isFirstEdit = text.toString().isEmpty()
        super.setText(text, type)
    }
}