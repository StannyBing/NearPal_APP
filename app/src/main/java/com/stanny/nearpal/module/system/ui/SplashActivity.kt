package com.stanny.nearpal.module.system.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.stanny.nearpal.R
import com.stanny.nearpal.base.BaseActivity
import com.stanny.nearpal.base.UserManager
import com.stanny.nearpal.base.setHead
import com.stanny.nearpal.base.toJson
import com.stanny.nearpal.module.letter.ui.MainActivity
import com.stanny.nearpal.module.system.bean.UserBean
import com.stanny.nearpal.module.system.mvp.contract.SplashContract
import com.stanny.nearpal.module.system.mvp.model.SplashModel
import com.stanny.nearpal.module.system.mvp.presenter.SplashPresenter
import com.umeng.message.IUmengCallback
import com.umeng.message.PushAgent
import kotlinx.android.synthetic.main.activity_splash.*


/**
 * Create By admin On 2017/7/11
 * 功能：
 */
class SplashActivity : BaseActivity<SplashPresenter, SplashModel>(), SplashContract.View {

    companion object {
        /**
         * 启动器
         */
        fun startAction(activity: Activity, isFinish: Boolean) {
            val intent = Intent(activity, SplashActivity::class.java)
            activity.startActivity(intent)
            if (isFinish) activity.finish()
        }
    }

    /**
     * layout配置
     */
    override fun getLayoutId(): Int {
        return R.layout.activity_splash
    }

    /**
     * 初始化
     */
    override fun initView(savedInstanceState: Bundle?) {

        if (UserManager.user?.headicon != null && UserManager.user?.headicon != 0) {
            iv_splash_icon.setHead(UserManager.user!!.headicon)
        }
        super.initView(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        PushAgent.getInstance(this).disable(object : IUmengCallback{
            override fun onSuccess() {
            }
            override fun onFailure(p0: String?, p1: String?) {
            }
        })
        handler.postDelayed({
            if (UserManager.user?.logintype == 0 && UserManager.userName.isNotEmpty() && UserManager.passWord.isNotEmpty()) {
                mPresenter.loginApp(hashMapOf("username" to UserManager.userName, "password" to UserManager.passWord).toJson())
            } else if (UserManager.user?.logintype == 5 && UserManager.id.isNotEmpty()) {
                mPresenter.visitLogin(hashMapOf("id" to UserManager.user!!.id).toJson())
            } else {
                LoginActivity.startAction(this, true)
            }
        }, 1000)
    }

    /**
     * View事件设置
     */
    override fun onViewListener() {

    }

    override fun onLoginResult(userBean: UserBean?) {
        if (userBean == null) {
            UserManager.loginOut()
            LoginActivity.startAction(this, true)
        } else {
            UserManager.user = userBean
            MainActivity.startAction(this, true)
        }
    }

}
