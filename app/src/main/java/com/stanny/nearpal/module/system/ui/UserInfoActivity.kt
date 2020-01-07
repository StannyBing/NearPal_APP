package com.stanny.nearpal.module.system.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.DatePicker
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.stanny.nearpal.R
import com.stanny.nearpal.base.BaseActivity
import com.stanny.nearpal.base.UserManager
import com.stanny.nearpal.base.setHead
import com.stanny.nearpal.base.toJson
import com.stanny.nearpal.module.letter.ui.MainActivity
import com.stanny.nearpal.module.system.bean.UserBean
import com.stanny.nearpal.module.system.func.HeadiconAdapter
import com.stanny.nearpal.module.system.mvp.contract.UserInfoContract
import com.stanny.nearpal.module.system.mvp.model.UserInfoModel
import com.stanny.nearpal.module.system.mvp.presenter.UserInfoPresenter
import com.zx.bui.ui.buidialog.BUIDialog
import com.zx.zxutils.ZXApp
import com.zx.zxutils.util.ZXTimeUtil
import kotlinx.android.synthetic.main.activity_user_info.*


/**
 * Create By admin On 2017/7/11
 * 功能：用户信息
 */
class UserInfoActivity : BaseActivity<UserInfoPresenter, UserInfoModel>(), UserInfoContract.View {

    private var userBirthday: Long? = null
    private var selectHeadIconId = 0

    private var justModify = false

    companion object {
        /**
         * 启动器
         */
        fun startAction(activity: Activity, isFinish: Boolean, justModify: Boolean = false) {
            val intent = Intent(activity, UserInfoActivity::class.java)
            intent.putExtra("justModify", justModify)
            activity.startActivity(intent)
            if (isFinish) activity.finish()
        }
    }

    /**
     * layout配置
     */
    override fun getLayoutId(): Int {
        return R.layout.activity_user_info
    }

    /**
     * 初始化
     */
    override fun initView(savedInstanceState: Bundle?) {

        justModify = intent.getBooleanExtra("justModify", false)
        selectHeadIconId = UserManager.user?.headicon ?: 0
        iv_userinfo_headicon.setHead(UserManager.user?.headicon)
        if (UserManager.user?.nickname != UserManager.user?.username) {
            et_userinfo_nickname.setText(UserManager.user?.nickname ?: "")
        }
        if (UserManager.user?.birthday?.isNotEmpty() == true) {
            userBirthday = ZXTimeUtil.string2Millis(UserManager.user?.birthday)
            tv_userinfo_birthday.text = UserManager.user?.birthday?.substring(0, 10)
        }
        if (UserManager.user?.sex?.isNotEmpty() == true) {
            tv_userinfo_sex.text = UserManager.user?.sex ?: ""
        }
        super.initView(savedInstanceState)
    }

    /**
     * View事件设置
     */
    override fun onViewListener() {
        iv_userinfo_close.setOnClickListener {
            if (!justModify) {
                UserManager.loginOut()
            }
            finish()
        }

        ttv_userinfo_next.setOnClickListener {
            if (et_userinfo_nickname.text.toString().isEmpty()) {
                showToast("请输入您的昵称（必填）")
            } else {
                UserManager.user?.let {
                    mPresenter.modifyUser(
                        hashMapOf(
                            "nickname" to et_userinfo_nickname.text.toString(),
                            "birthday" to if (userBirthday == null) "0" else userBirthday.toString(),
                            "sex" to tv_userinfo_sex.text.toString(),
                            "headicon" to selectHeadIconId.toString()
                        ).toJson()
                    )
                }
            }
        }

        tv_userinfo_birthday.setOnClickListener {
            val view = LayoutInflater.from(this).inflate(R.layout.layout_date_picker, null)
            val datePicker = view.findViewById<DatePicker>(R.id.date_picker)
            if (tv_userinfo_birthday.text.isNotEmpty()) {
                val birthdayInfo = tv_userinfo_birthday.text.split("-")
                datePicker.updateDate(
                    Integer.parseInt(birthdayInfo[0]),
                    Integer.parseInt(birthdayInfo[1]) - 1,
                    Integer.parseInt(birthdayInfo[2])
                )
            }
            BUIDialog.showCustom(this, view = view, btnBuidler = BUIDialog.BtnBuilder()
                .withCancelBtn()
                .withSubmitBtn {
                    tv_userinfo_birthday.text =
                        "${datePicker.year}-${datePicker.month + 1}-${datePicker.dayOfMonth}"
                    userBirthday = ZXTimeUtil.string2Millis(
                        "${datePicker.year}-${datePicker.month + 1}-${datePicker.dayOfMonth}",
                        "yyyy-MM-dd"
                    )
                }
            )
        }

        tv_userinfo_sex.setOnClickListener {
            BUIDialog.showSimpleList(this, "我认为我是：", arrayListOf<BUIDialog.ListBean>().apply {
                add(BUIDialog.ListBean("男生"))
                add(BUIDialog.ListBean("女生"))
            }, {
                tv_userinfo_sex.text = it.key
            })
        }

        iv_userinfo_headicon.setOnClickListener {
            val view = LayoutInflater.from(this).inflate(R.layout.layout_userinfo_headicon, null)
            val rvHeadicon = view.findViewById<RecyclerView>(R.id.rv_headicon)
            val dialog = BUIDialog.showCustom(
                this, "请选择头像", view, BUIDialog.BtnBuilder()
                    .withCancelBtn()
            )
            rvHeadicon.apply {
                layoutManager = GridLayoutManager(this@UserInfoActivity, 3)
                adapter = HeadiconAdapter().apply {
                    setOnItemClickListener { adapter, view, position ->
                        selectHeadIconId = data[position].id
                        Glide.with(ZXApp.getContext())
                            .load(data[position].iconResId)
                            .apply(
                                RequestOptions()
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .circleCrop()
                            )
                            .transition(DrawableTransitionOptions().crossFade())
                            .into(iv_userinfo_headicon)
                        dialog.dismiss()
                    }
                }
            }
        }
    }

    override fun onModifyResult(userBean: UserBean) {
        showToast("信息修改成功")
        UserManager.user = userBean
        if (justModify) {
            finish()
        } else {
            MainActivity.startAction(this, true)
        }
    }

}
