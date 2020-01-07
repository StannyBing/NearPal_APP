package com.stanny.nearpal.module.letter.func.adapter

import android.widget.ImageView
import com.stanny.nearpal.R
import com.stanny.nearpal.base.setHead
import com.stanny.nearpal.module.system.bean.UserBean
import com.zx.zxutils.other.QuickAdapter.ZXBaseHolder
import com.zx.zxutils.other.QuickAdapter.ZXQuickAdapter

/**
 * Created by Xiangb on 2019/12/11.
 * 功能：
 */
class PenpalAdapter(dataList: List<UserBean?>) :
    ZXQuickAdapter<UserBean, ZXBaseHolder>(R.layout.item_main_penpal, dataList) {
    override fun convert(helper: ZXBaseHolder, item: UserBean?) {
        if (item != null) {
            helper.getView<ImageView>(R.id.iv_mainPenpal_headIcon)
                .setHead(item.headicon)
            helper.setText(R.id.tv_mainPenpal_username, item.nickname)
        }
    }

}