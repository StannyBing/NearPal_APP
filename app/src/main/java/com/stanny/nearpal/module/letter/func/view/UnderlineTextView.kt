package com.stanny.nearpal.module.letter.func.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.stanny.nearpal.R

/**
 * Created by Xiangb on 2017/11/30.
 * 功能：下滑线
 */
class UnderlineTextView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : AppCompatTextView(context, attrs, defStyleAttr) {
    private val paint: Paint
    var isIndent = false
    var color: Int

    init {
        val spaceExtra: Float = getLineSpacingExtra()
        val spaceMulti: Float = getLineSpacingMultiplier()
        if (spaceExtra == 0f && spaceMulti == 1f) {
            setLineSpacing(20f, 1f)
        }
        setPadding(
                paddingLeft,
                paddingTop,
                right,
                if (spaceExtra == 0f) 20 else spaceExtra.toInt()
        )
        color = ContextCompat.getColor(context, R.color.gray)
        try {
            val typeArray =
                    context.obtainStyledAttributes(attrs, R.styleable.UnderlineTextView)
            color = typeArray.getColor(
                    R.styleable.UnderlineTextView_lineColor,
                    ContextCompat.getColor(context, R.color.gray)
            )
            isIndent = typeArray.getBoolean(R.styleable.UnderlineTextView_withIndent, false)
            typeArray.recycle()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        paint = Paint()
        paint.color = color
    }

    override fun setText(text: CharSequence, type: TextView.BufferType?) {
        if (isIndent) {
            super.setText("      $text", type) //首行缩进，由于要6个字符位，所以采用这种方式
        } else {
            super.setText(text, type)
        }
    }

    override fun onDraw(canvas: Canvas?) {
        val fm = getPaint().fontMetrics
        val textHeight = Math.ceil(fm.descent - fm.ascent.toDouble())
        for (i in 0 until (height / lineHeight) as Int) {
            //            canvas.drawLine(0, (i + 1) * lineHeight - 5, getWidth(), (i + 1) * lineHeight - 5, paint);
            //根据文本的高度决定下划线的位置
            canvas?.drawLine(
                    0f,
                    (i + 1) * lineHeight - (lineHeight - textHeight).toFloat() / 2,
                    width.toFloat(),
                    (i + 1) * lineHeight - (lineHeight - textHeight).toFloat() / 2,
                    paint
            )
        }
        super.onDraw(canvas)
    }

}