package com.stanny.nearpal.module.system.func

import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.stanny.nearpal.R
import com.stanny.nearpal.module.system.bean.HeadiconBean
import com.zx.zxutils.ZXApp
import com.zx.zxutils.other.QuickAdapter.ZXBaseHolder
import com.zx.zxutils.other.QuickAdapter.ZXQuickAdapter

/**
 * Created by Xiangb on 2019/12/18.
 * 功能：
 */
class HeadiconAdapter : ZXQuickAdapter<HeadiconBean, ZXBaseHolder>(R.layout.item_headicon) {

    init {
        mData = arrayListOf<HeadiconBean>().apply {
            add(HeadiconBean(1, R.drawable.head1))
            add(HeadiconBean(2, R.drawable.head2))
            add(HeadiconBean(3, R.drawable.head3))
            add(HeadiconBean(4, R.drawable.head4))
            add(HeadiconBean(5, R.drawable.head5))
            add(HeadiconBean(6, R.drawable.head6))
            add(HeadiconBean(7, R.drawable.head7))
            add(HeadiconBean(8, R.drawable.head8))
            add(HeadiconBean(9, R.drawable.head9))
            add(HeadiconBean(10, R.drawable.head10))
            add(HeadiconBean(11, R.drawable.head11))
            add(HeadiconBean(12, R.drawable.head12))
            add(HeadiconBean(13, R.drawable.head13))
            add(HeadiconBean(14, R.drawable.head14))
            add(HeadiconBean(15, R.drawable.head15))
            add(HeadiconBean(16, R.drawable.head16))
            add(HeadiconBean(17, R.drawable.head17))
            add(HeadiconBean(18, R.drawable.head18))
            add(HeadiconBean(19, R.drawable.head19))
            add(HeadiconBean(20, R.drawable.head20))
        }
    }

    override fun convert(helper: ZXBaseHolder, item: HeadiconBean?) {
        item?.let {
            Glide.with(ZXApp.getContext())
                .load(it.iconResId)
                .apply(
                    RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .circleCrop()
                )
                .transition(DrawableTransitionOptions().crossFade())
                .into(helper.getView(R.id.iv_headicon))
        }
    }
}