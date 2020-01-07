package com.stanny.nearpal.module.letter.func.tool

import android.content.Context
import android.content.res.Resources
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.zx.zxutils.util.ZXScreenUtil


/**
 * Created by Xiangb on 2019/12/30.
 * 功能：
 */
class MyItemDecoration(context: Context) : ItemDecoration() {
    var mPageMargin = 5 //自定义默认item边距
    var mLeftPageVisibleWidth //第一张图片的左边距
            : Int

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val positon = parent.getChildAdapterPosition(view) //获得当前item的position
        val itemCount = parent.adapter!!.itemCount //获得item的数量
        val leftMargin: Int
        leftMargin = if (positon == 0) {
            dpToPx(mLeftPageVisibleWidth)
        } else {
            dpToPx(mPageMargin)
        }
        val rightMargin: Int
        rightMargin = if (positon == itemCount - 1) {
            dpToPx(mLeftPageVisibleWidth)
        } else {
            dpToPx(mPageMargin)
        }
        val lp = view.getLayoutParams() as RecyclerView.LayoutParams
        lp.setMargins(leftMargin, 30, rightMargin, 60)
        view.setLayoutParams(lp)
        super.getItemOffsets(outRect, view, parent, state)
    }

    private fun dpToPx(dp: Int): Int {
        return (dp * Resources.getSystem().displayMetrics.density + 0.5f).toInt() //dp转px
    }

    init {
        mLeftPageVisibleWidth =
            (((ZXScreenUtil.getScreenWidth() / Resources.getSystem().getDisplayMetrics().density - 300 - mPageMargin) / 2).toInt())
    }
}
