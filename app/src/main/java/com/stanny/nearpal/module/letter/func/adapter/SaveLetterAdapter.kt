package com.stanny.nearpal.module.letter.func.adapter

import com.stanny.nearpal.R
import com.stanny.nearpal.module.letter.bean.LetterBean
import com.zx.zxutils.other.QuickAdapter.ZXBaseHolder
import com.zx.zxutils.other.QuickAdapter.ZXQuickAdapter

/**
 * Created by Xiangb on 2020/1/8.
 * 功能：
 */
class SaveLetterAdapter(dataList: List<LetterBean>) :
    ZXQuickAdapter<LetterBean, ZXBaseHolder>(R.layout.item_save_letter, dataList) {
    override fun convert(helper: ZXBaseHolder, item: LetterBean?) {
        if (item != null) {
            helper.setText(R.id.tv_save_info, "写给:${item.nickname ?: "随机信件"}\n(${item.sendtime})")

            helper.addOnClickListener(R.id.iv_save_delete)
        }
    }
}