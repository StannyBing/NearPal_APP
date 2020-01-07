package com.stanny.nearpal.module.letter.func.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.stanny.nearpal.R
import com.zx.zxutils.util.ZXSystemUtil

/**
 * Created by Xiangb on 2017/11/7.
 * 功能：邮编界面
 */
class PostCodeView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) : AppCompatTextView(context, attrs, defStyle) {
    private var codeText = ""
    private var frameWidth = 0
    private var frameSpace = 0
    private var frameColor = 0
    private var codeColor = 0
    private var codeTextSize = 0
    private var framePaint: Paint? = null
    private var codePaint: Paint? = null

    init {
        frameWidth = ZXSystemUtil.dp2px(20f)
        frameSpace = ZXSystemUtil.dp2px(5f)
        frameColor = ContextCompat.getColor(context, R.color.gray)
        codeTextSize = ZXSystemUtil.sp2px(8f)
        codeColor = ContextCompat.getColor(context, R.color.blue)
        val typedArray =
                context.obtainStyledAttributes(attrs, R.styleable.PostCodeView)
        try {
            codeText = typedArray.getString(R.styleable.PostCodeView_codeText)
            frameWidth = typedArray.getDimensionPixelSize(
                    R.styleable.PostCodeView_frameWidth,
                    ZXSystemUtil.dp2px(20f)
            )
            frameSpace = typedArray.getDimensionPixelSize(
                    R.styleable.PostCodeView_frameSpace,
                    ZXSystemUtil.dp2px(5f)
            )
            frameColor = typedArray.getColor(
                    R.styleable.PostCodeView_frameColor,
                    ContextCompat.getColor(context, R.color.gray)
            )
            codeColor = typedArray.getColor(
                    R.styleable.PostCodeView_codeTextColor,
                    ContextCompat.getColor(context, R.color.blue)
            )
            codeTextSize = typedArray.getDimensionPixelSize(
                    R.styleable.PostCodeView_codeTextSize,
                    ZXSystemUtil.sp2px(8f)
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
        typedArray.recycle()
        framePaint = Paint()
        codePaint = Paint()
    }

    fun setCodeText(codeText: String): PostCodeView {
        this.codeText = codeText
        postInvalidate()
        return this
    }

    fun setFrameWidth(frameWidthDp: Int): PostCodeView {
        frameWidth = ZXSystemUtil.dp2px(frameWidthDp.toFloat())
        postInvalidate()
        return this
    }

    fun setFrameSpace(frameSpaceDp: Int): PostCodeView {
        frameSpace = ZXSystemUtil.dp2px(frameSpaceDp.toFloat())
        postInvalidate()
        return this
    }

    fun setFrameColor(frameColor: Int): PostCodeView {
        this.frameColor = ContextCompat.getColor(context, frameColor)
        postInvalidate()
        return this
    }

    fun setCodeColor(codeColor: Int): PostCodeView {
        this.codeColor = ContextCompat.getColor(context, codeColor)
        postInvalidate()
        return this
    }

    fun setCodeTextSize(codeTextSizeSp: Int): PostCodeView {
        codeTextSize = ZXSystemUtil.sp2px(codeTextSizeSp.toFloat())
        postInvalidate()
        return this
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        framePaint!!.color = frameColor
        framePaint!!.isAntiAlias = true
        framePaint!!.strokeWidth = 1f
        framePaint!!.style = Paint.Style.STROKE
        codePaint!!.color = codeColor
        codePaint!!.isAntiAlias = true
        codePaint!!.textSize = codeTextSize.toFloat()
        for (i in 0 until codeText.length) {
            val rect = RectF(((frameWidth + frameSpace) * i + 1).toFloat(), 1f, ((frameWidth + frameSpace) * i + frameWidth).toFloat(), frameWidth.toFloat())
            canvas.drawRoundRect(rect, 10f, 10f, framePaint)
            canvas.drawText(codeText[i].toString(), (frameWidth + frameSpace) * i + frameWidth / 2 - codeTextSize / 3 + 2.toFloat(), frameWidth / 2 + codeTextSize / 3.toFloat(), codePaint)
        }
    }
}