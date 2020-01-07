package com.stanny.nearpal.module.system.func

import com.stanny.nearpal.R
import com.zx.zxutils.other.QuickAdapter.ZXBaseHolder
import com.zx.zxutils.other.QuickAdapter.ZXQuickAdapter

/**
 * Created by Xiangb on 2019/12/27.
 * 功能：
 */
class MenuAdapter(dataList: List<String>) :
    ZXQuickAdapter<String, ZXBaseHolder>(R.layout.item_menu, dataList) {
    override fun convert(helper: ZXBaseHolder, item: String?) {
        if (item != null) {
            helper.setText(R.id.tv_menu_name, item)
        }
    }
}