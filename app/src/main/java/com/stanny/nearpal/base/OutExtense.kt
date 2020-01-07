package com.stanny.nearpal.base

import android.graphics.Paint
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.google.gson.Gson
import com.stanny.nearpal.R
import com.zx.zxutils.ZXApp
import okhttp3.MediaType
import okhttp3.RequestBody

/**
 * Created by Xiangb on 2019/12/11.
 * 功能：
 */
fun ImageView.setHead(headIcon: Int?) {
    val resId = if (headIcon == null || headIcon == 0) {
        R.mipmap.ic_launcher
    } else {
        resources.getIdentifier(
            "head${headIcon}",
            "drawable",
            context.packageName
        )
    }
    Glide.with(ZXApp.getContext())
        .load(resId)
        .apply(
            RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .circleCrop()
        )
        .transition(DrawableTransitionOptions().crossFade())
        .into(this)
}

fun Paint.getBaseline(): Float {
    return (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom
}

fun <K, Y> Map<K, Y>.toJson(): RequestBody {
    val json = Gson().toJson(this)
    return RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json)
}