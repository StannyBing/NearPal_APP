package com.stanny.nearpal.module.letter.func.tool

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.zx.zxutils.util.ZXScreenUtil

/**
 * Created by Xiangb on 2019/12/30.
 * 功能：
 */
object MySnapHelper {
    fun bindRecycler(context: Context, recyclerView: RecyclerView, change: (Int) -> Unit) {
        recyclerView.addItemDecoration(MyItemDecoration(context))
        val pagerSnapHelper: PagerSnapHelper = object : PagerSnapHelper() {
            override fun findTargetSnapPosition(
                layoutManager: RecyclerView.LayoutManager,
                velocityX: Int,
                velocityY: Int
            ): Int {
                val targetPos =
                    super.findTargetSnapPosition(layoutManager, velocityX, velocityY)
                change(targetPos)
                return targetPos
            }
        }

        pagerSnapHelper.attachToRecyclerView(recyclerView)

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                // 移动bg
                val childCount: Int = recyclerView.getChildCount()
                for (i in 0 until childCount) {
                    val child =
                        recyclerView.getChildAt(i) as ViewGroup
                    val lp =
                        child.layoutParams as RecyclerView.LayoutParams
                    // lp.rightMargin = 5;
                    // lp.height = 200;
                    // lp.rightMargin = 5;
                    // lp.height = 200;
                    val left = child.left
                    val right: Int = ZXScreenUtil.getScreenWidth() - child.right
                    val percent: Float = if (left < 0 || right < 0) 0f else Math.min(
                        left,
                        right
                    ) * 1f / Math.max(left, right)
                    val scaleFactor: Float =
                        0.95f + Math.abs(percent) * (1.05f - 0.95f)

                    child.layoutParams = lp
                    child.scaleY = scaleFactor
                    child.scaleX = scaleFactor
                }
            }
        })
    }
}