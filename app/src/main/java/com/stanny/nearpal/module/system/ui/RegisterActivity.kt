package com.stanny.nearpal.module.system.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.stanny.nearpal.R
import com.stanny.nearpal.base.BaseActivity
import com.stanny.nearpal.base.UserManager
import com.stanny.nearpal.base.toJson
import com.stanny.nearpal.module.system.bean.UserBean
import com.stanny.nearpal.module.system.mvp.contract.RegisterContract
import com.stanny.nearpal.module.system.mvp.model.RegisterModel
import com.stanny.nearpal.module.system.mvp.presenter.RegisterPresenter
import com.zx.zxutils.util.ZXMD5Util
import com.zx.zxutils.util.ZXSystemUtil
import kotlinx.android.synthetic.main.activity_register.*


/**
 * Create By admin On 2017/7/11
 * 功能：用户注册界面
 */
class RegisterActivity : BaseActivity<RegisterPresenter, RegisterModel>(), RegisterContract.View {

    companion object {
        /**
         * 启动器
         */
        fun startAction(activity: Activity, isFinish: Boolean) {
            val intent = Intent(activity, RegisterActivity::class.java)
            activity.startActivityForResult(intent, 0x01)
            if (isFinish) activity.finish()
        }
    }

    /**
     * layout配置
     */
    override fun getLayoutId(): Int {
        return R.layout.activity_register
    }

    /**
     * 初始化
     */
    override fun initView(savedInstanceState: Bundle?) {
        if (UserManager.id.isNotEmpty()) {
            tv_visit_tips.visibility = View.VISIBLE
            ttv_register_next.text = "完成"
        }
        super.initView(savedInstanceState)
    }

    /**
     * View事件设置
     */
    override fun onViewListener() {
        iv_register_close.setOnClickListener { finish() }

        ttv_register_next.setOnClickListener {
            if (et_register_username.text.toString().isEmpty()) {
                showToast("请输入用户名")
            } else if (et_register_password1.text.toString().isEmpty() || et_register_password2.text.toString().isEmpty()) {
                showToast("请输入密码")
            } else if (et_register_password1.text.toString() != et_register_password2.text.toString()) {
                showToast("两次密码输入不一致")
            } else {
                ZXSystemUtil.closeKeybord(this)
                mPresenter.doRegister(
                    hashMapOf(
                        "username" to et_register_username.text.toString(),
                        "password" to ZXMD5Util.getMD5(et_register_password1.text.toString()),
                        "telephone" to et_register_telephone.text.toString(),
                        "userid" to UserManager.id//若id存在，则是为游客转注册账户
                    ).toJson()
                )
            }
        }
    }

    override fun onRegisterResult(userBean: UserBean?) {
        if (userBean != null) {
            UserManager.user = userBean
            UserManager.userName = et_register_username.text.toString()
            UserManager.passWord = ZXMD5Util.getMD5(et_register_password1.text.toString())
            showToast("注册成功")
            if (UserManager.id.isNotEmpty()) {
                setResult(0x01)
                finish()
            } else {
                UserInfoActivity.startAction(this, true)
            }
        }
    }
}
