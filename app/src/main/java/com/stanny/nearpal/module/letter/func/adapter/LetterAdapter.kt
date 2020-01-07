package com.stanny.nearpal.module.letter.func.adapter

import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.stanny.nearpal.R
import com.stanny.nearpal.base.UserManager
import com.stanny.nearpal.base.setHead
import com.stanny.nearpal.module.letter.bean.LetterBean
import com.stanny.nearpal.module.letter.func.view.PostCodeView
import com.stanny.nearpal.module.letter.func.view.UnderlineTextView
import com.zx.zxutils.other.QuickAdapter.ZXBaseHolder
import com.zx.zxutils.other.QuickAdapter.ZXQuickAdapter
import com.zx.zxutils.util.ZXTimeUtil
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.util.concurrent.TimeUnit

/**
 * Created by Xiangb on 2019/12/30.
 * 功能：
 */
class LetterAdapter(dataList: List<LetterBean>) :
    ZXQuickAdapter<LetterBean, ZXBaseHolder>(R.layout.item_cardviewpager, dataList) {

    override fun convert(helper: ZXBaseHolder, item: LetterBean?) {
        if (item != null) {
            val pcvPostCode = helper.getView<PostCodeView>(R.id.pcv_card_postcode) //邮编
            val ivStamp = helper.getView<ImageView>(R.id.iv_card_stamp)//邮票
            val ultvInfo = helper.getView<UnderlineTextView>(R.id.ultv_card_info)  //信息
            val ivNotRead = helper.getView<ImageView>(R.id.iv_card_notread) //未读
            val ivFromIcon = helper.getView<ImageView>(R.id.iv_card_fromIcon)  //发信人头像
            val tvFromName = helper.getView<TextView>(R.id.tv_card_fromName) //发信人名称
            val tvDate = helper.getView<TextView>(R.id.tv_card_date)  //时间
            val llRandom = helper.getView<LinearLayout>(R.id.ll_letter_random)//随机信件
            val tvRandom = helper.getView<TextView>(R.id.tv_letter_random)//随机信件文字
            val ivRandom = helper.getView<ImageView>(R.id.iv_letter_random)//随机信件图片

            pcvPostCode.setCodeText(item.postcode)
            ultvInfo.text = item.letterinfo
            tvDate.text = item.sendtime
            tvFromName.text = item.nickname
            ivFromIcon.setHead(item.headicon)

            llRandom.visibility = View.GONE

            if (item.mstatus == 1) {
                llRandom.visibility = View.VISIBLE
                ivRandom.startAnimation(
                    ScaleAnimation(
                        1.0f,
                        0.7f,
                        1.0f,
                        0.7f,
                        ScaleAnimation.RELATIVE_TO_SELF,
                        0.5f,
                        ScaleAnimation.RELATIVE_TO_SELF,
                        0.5f
                    ).apply {
                        duration = 400
                        interpolator = AccelerateDecelerateInterpolator()
                        repeatCount = Animation.INFINITE
                        repeatMode = Animation.REVERSE
                    })
                val lastSecond =
                    ZXTimeUtil.string2Millis(item.accepttime) / 1000 + 60 * 60 * 24 - System.currentTimeMillis() / 1000
                Observable.interval(0, 1L, TimeUnit.SECONDS)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        tvRandom.text =
                            "随机信件\n${((lastSecond - it) / 60 / 60).toString().padStart(2, '0')}" +
                                    ":${((lastSecond - it) / 60 % 60).toString().padStart(
                                        2,
                                        '0'
                                    )}" +
                                    ":${((lastSecond - it) % 60 % 60).toString().padStart(
                                        2,
                                        '0'
                                    )}后消失"
                    }
                llRandom.visibility = View.VISIBLE
            }
            when (item.mstatus) {
                3 -> {
                    ivNotRead.visibility = View.GONE
                }
                else -> {
                    if (item.senduserid != UserManager.user?.id) {
                        ivNotRead.visibility = View.VISIBLE
                    } else {
                        ivNotRead.visibility = View.GONE
                    }
                    llRandom.visibility = View.GONE
                }
            }
        }
    }

}