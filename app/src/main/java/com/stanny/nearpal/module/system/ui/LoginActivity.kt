package com.stanny.nearpal.module.system.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.frame.zxmvp.baseapp.BaseApplication
import com.stanny.nearpal.R
import com.stanny.nearpal.base.BaseActivity
import com.stanny.nearpal.base.UserManager
import com.stanny.nearpal.base.setHead
import com.stanny.nearpal.base.toJson
import com.stanny.nearpal.module.letter.ui.MainActivity
import com.stanny.nearpal.module.system.bean.UserBean
import com.stanny.nearpal.module.system.mvp.contract.LoginContract
import com.stanny.nearpal.module.system.mvp.model.LoginModel
import com.stanny.nearpal.module.system.mvp.presenter.LoginPresenter
import com.zx.bui.ui.buidialog.BUIDialog
import com.zx.zxutils.util.ZXMD5Util
import kotlinx.android.synthetic.main.activity_login.*


/**
 * Create By admin On 2017/7/11
 * 功能：
 */
class LoginActivity : BaseActivity<LoginPresenter, LoginModel>(), LoginContract.View {

    companion object {
        /**
         * 启动器
         */
        fun startAction(activity: Activity, isFinish: Boolean) {
            val intent = Intent(activity, LoginActivity::class.java)
            activity.startActivity(intent)
            if (isFinish) activity.finish()
        }
    }


    /**
     * layout配置
     */
    override fun getLayoutId(): Int {
        return R.layout.activity_login
    }

    /**
     * 初始化
     */
    override fun initView(savedInstanceState: Bundle?) {
        UserManager.loginOut()
        if (UserManager.userName.isNotEmpty()) {
            et_login_username.setText(UserManager.userName)
        }
        if (UserManager.user?.headicon != null && UserManager.user?.headicon != 0) {
            iv_login_headicon.setHead(UserManager.user!!.headicon)
        }
        super.initView(savedInstanceState)

    }

    /**
     * View事件设置
     */
    override fun onViewListener() {

//        if (intent.getBooleanExtra("justLogin", false)) {
//            MainActivity.startAction(this, true)
//        } else {
//            finish()
//        }

        ll_login_wechat.setOnClickListener {
            showToast("暂未开放，请先使用账号注册")
            //            MainActivity.startAction(
//                    this,
//                    true
//            )
        }
        ll_login_QQ.setOnClickListener {
            showToast("暂未开放，请先使用账号注册")
            //            MainActivity.startAction(
//                    this,
//                    true
//            )
        }
        ll_login_weibo.setOnClickListener {
            showToast("暂未开放，请先使用账号注册")
            //            MainActivity.startAction(
//                    this,
//                    true
//            )
        }
        ll_login_phone.setOnClickListener {
            showToast("暂未开放，请先使用账号注册")
            //            MainActivity.startAction(
//                    this,
//                    true
//            )
        }
        tv_login_register.setOnClickListener { RegisterActivity.startAction(this, false) }

        tv_login_visitor.setOnClickListener {
            BUIDialog.showInfo(this,
                "提示",
                "是否注册游客账户（可随时携带信息转为注册账户）",
                BUIDialog.BtnBuilder().withCancelBtn()
                    .withSubmitBtn {
                        mPresenter.visitRegister()
                    })
        }

        btn_login_submit.setOnClickListener {
            if (et_login_username.text.toString().isEmpty()) {
                showToast("请输入用户名/手机号")
            } else if (et_login_password.text.toString().isEmpty()) {
                showToast("请输入密码")
            } else {
                mPresenter.loginApp(
                    hashMapOf(
                        "username" to et_login_username.text.toString(),
                        "password" to ZXMD5Util.getMD5(et_login_password.text.toString())
                    ).toJson()
                )
            }
        }
    }

    override fun onLoginResult(userBean: UserBean) {
        UserManager.user = userBean
        UserManager.userName = et_login_username.text.toString()
        UserManager.passWord = ZXMD5Util.getMD5(et_login_password.text.toString())
        showToast("登录成功")
        MainActivity.startAction(this, false)
    }

    override fun onVisitRegisterResult(userBean: UserBean) {
        UserManager.user = userBean
        UserManager.id = userBean.id.toString()
        showToast("注册成功")
        UserInfoActivity.startAction(this, false)
    }

    override fun onBackPressed() {
        BaseApplication.baseApplication.exit()
        super.onBackPressed()
    }
}
