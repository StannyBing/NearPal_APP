package com.stanny.nearpal.app

import android.graphics.Typeface
import androidx.core.content.ContextCompat
import cn.jpush.android.api.JPushInterface
import com.frame.zxmvp.baseapp.BaseApplication
import com.stanny.nearpal.BuildConfig
import com.stanny.nearpal.R
import com.tencent.bugly.Bugly
import com.tencent.bugly.beta.Beta
import com.zx.bui.BUIConfig
import com.zx.zxutils.ZXApp


/**
 * Created by Xiangb on 2019/12/9.
 * 功能：
 */
class MyApplication : BaseApplication() {

    companion object {
        var typeFace: Typeface? = null
        var deviceToken = ""
    }


    override fun onCreate() {
        super.onCreate()
        ZXApp.init(this, !BuildConfig.ISRELEASE)
        BUIConfig.uiSize = resources.getDimension(R.dimen.text_normal_size)
        BUIConfig.uiColor = ContextCompat.getColor(this, R.color.letter_blue)
        //初始化bugly
        Beta.enableNotification = true
        Beta.canShowApkInfo = false
//        Beta.tipsDialogLayoutId = R.layout.layout_app_update
//        Beta.upgradeDialogLayoutId = R.layout.layout_app_update
        Bugly.init(this, "db65a0b90f", !BuildConfig.ISRELEASE)

        //配置极光推送
        JPushInterface.setDebugMode(!BuildConfig.ISRELEASE)
        JPushInterface.init(this)//初始化JPush
        JPushInterface.setLatestNotificationNumber(this, 1)

        typeFace = Typeface.createFromAsset(assets, "simkai.ttf")

//        Beta.upgradeListener =
//            UpgradeListener { ret, strategy, isManual, isSilence ->
//                if (strategy != null) {
////                    val i = Intent()
////                    i.setClass(applicationContext, UpgradeActivity::class.java)
////                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
////                    startActivity(i)
//                } else {
//                    ZXToastUtil.showToast("没有更新")
//                }
//            }
    }

}