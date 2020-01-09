package com.stanny.nearpal.app

import android.content.Context
import android.graphics.Typeface
import androidx.core.content.ContextCompat
import com.frame.zxmvp.baseapp.BaseApplication
import com.stanny.nearpal.BuildConfig
import com.stanny.nearpal.R
import com.stanny.nearpal.module.letter.ui.MainActivity
import com.tencent.bugly.Bugly
import com.tencent.bugly.beta.Beta
import com.umeng.analytics.MobclickAgent
import com.umeng.commonsdk.UMConfigure
import com.umeng.message.IUmengRegisterCallback
import com.umeng.message.PushAgent
import com.umeng.message.UmengMessageHandler
import com.umeng.message.entity.UMessage
import com.zx.bui.BUIConfig
import com.zx.zxutils.ZXApp
import com.zx.zxutils.util.ZXLogUtil


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

        //配置Bugly
        Beta.canShowUpgradeActs.add(MainActivity::class.java)
        Bugly.init(this, "db65a0b90f", !BuildConfig.ISRELEASE)

        //配置友盟
        UMConfigure.init(
            this,
            "5dfb27c40cafb21204000253",
            "UMENG",
            UMConfigure.DEVICE_TYPE_PHONE,
            "9a03f06c31f330059b72a252a76f60f9"
        )
        PushAgent.getInstance(this).apply {
            //注册监听
            register(object : IUmengRegisterCallback {
                override fun onSuccess(p0: String?) {

                }

                override fun onFailure(p0: String?, p1: String?) {
                }
            })
            //自定义消息监听
            messageHandler = object : UmengMessageHandler(){
                override fun dealWithCustomMessage(p0: Context?, p1: UMessage?) {
                    ZXLogUtil.loge("获取到自定义消息")
                }
            }
            //检查集成配置文件
            isPushCheck = true
        }
        MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.AUTO)

        typeFace = Typeface.createFromAsset(assets, "simkai.ttf")

    }

}