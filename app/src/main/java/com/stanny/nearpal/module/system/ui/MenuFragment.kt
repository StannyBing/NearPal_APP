package com.stanny.nearpal.module.system.ui

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.digitalcq.zhyqsjpt.base.BaseFragment
import com.frame.zxmvp.baseapp.BaseApplication
import com.stanny.nearpal.R
import com.stanny.nearpal.base.UserManager
import com.stanny.nearpal.base.setHead
import com.stanny.nearpal.module.letter.ui.MainActivity
import com.stanny.nearpal.module.system.bean.UserBean
import com.stanny.nearpal.module.system.func.MenuAdapter
import com.stanny.nearpal.module.system.mvp.contract.MenuContract
import com.stanny.nearpal.module.system.mvp.model.MenuModel
import com.stanny.nearpal.module.system.mvp.presenter.MenuPresenter
import com.umeng.message.IUmengCallback
import com.umeng.message.PushAgent
import com.zx.bui.ui.buidialog.BUIDialog
import com.zx.zxutils.util.ZXBitmapUtil
import com.zx.zxutils.util.ZXSystemUtil
import kotlinx.android.synthetic.main.fragment_menu.*

/**
 * Create By admin On 2017/7/11
 * 功能：
 */
class MenuFragment : BaseFragment<MenuPresenter, MenuModel>(), MenuContract.View {

    private val menuList = arrayListOf<String>()
    private val menuAdapter = MenuAdapter(menuList)

    private var dialog: AlertDialog? = null

    companion object {
        /**
         * 启动器
         */
        fun newInstance(): MenuFragment {
            val fragment = MenuFragment()
            val bundle = Bundle()

            fragment.arguments = bundle
            return fragment
        }
    }

    /**
     * layout配置
     */
    override fun getLayoutId(): Int {
        return R.layout.fragment_menu
    }

    /**
     * 初始化
     */
    override fun initView(savedInstanceState: Bundle?) {
        if (UserManager.user?.logintype == 5) {//游客账户登录
            menuList.add("当前为游客登录，请及时注册")
        }

        menuList.add("微信公众号")
        menuList.add("意见反馈")
        menuList.add("退出登录")
        rv_menu.apply {
            layoutManager = LinearLayoutManager(activity!!)
            adapter = menuAdapter
        }
        setUserInfo()
        super.initView(savedInstanceState)
    }

    private fun setUserInfo() {
        if (UserManager.user?.headicon != null && UserManager.user?.headicon != 0) {
            iv_menu_headicon.setHead(UserManager.user!!.headicon)
        }
        tv_menu_balance.text = UserManager.user?.balance.toString()
        tv_menu_nickname.text = UserManager.user?.nickname
    }

    /**
     * View事件设置
     */
    override fun onViewListener() {
        iv_menu_balanceTips.setOnClickListener {
            BUIDialog.showInfo(
                activity!!,
                "提示",
                "每月一日，如剩余楮豆豆不足5颗，将自动补齐(短时间内不会开放购买，如果不够了，请反馈给我，我给你加。。。)"
            )
        }
        iv_menu_headicon.setOnClickListener {
            UserInfoActivity.startAction(activity!!, false, true)
        }
        menuAdapter.setOnItemClickListener { adapter, view, position ->
            when (menuList[position]) {
                "当前为游客登录，请及时注册" -> {
                    RegisterActivity.startAction(activity!!, false)
                }
                "微信公众号" -> {
                    val qrcodeView =
                        LayoutInflater.from(activity!!).inflate(R.layout.layout_qrcode_wx, null)
                    qrcodeView.findViewById<ImageView>(R.id.iv_qrcode_wx).setOnLongClickListener {
                        getPermission(
                            arrayOf(
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE
                            )
                        ) {
                            try {
                                MediaStore.Images.Media.insertImage(
                                    activity!!.contentResolver,
                                    ZXBitmapUtil.drawableToBitmap(
                                        ContextCompat.getDrawable(
                                            activity!!,
                                            R.drawable.arcode_wx
                                        )
                                    ),
                                    "楮先生App二维码",
                                    "楮先生App微信公众号二维码"
                                )
                                showToast("保存成功, 已存放在您的手机相册中了")
                            } catch (e: Exception) {
                            }
                        }
                        false
                    }
                    BUIDialog.showCustom(activity!!, "微信公众号", qrcodeView)
                }
                "意见反馈" -> {
                    val feedback =
                        LayoutInflater.from(activity!!).inflate(R.layout.layout_feedback, null)
                    val etContent = feedback.findViewById<EditText>(R.id.et_feedback)
                    dialog =
                        BUIDialog.showCustom(activity!!, "意见反馈", feedback, BUIDialog.BtnBuilder()
                            .withCancelBtn()
                            .withSubmitBtn {
                                if (etContent.text.toString().isNotEmpty()) {
                                    mPresenter.sendFeedBack(hashMapOf("feedcontent" to etContent.text.toString()))
                                }
                            })
                }
                "退出登录" -> {
                    BUIDialog.showInfo(
                        activity!!,
                        "提示",
                        if (UserManager.user?.logintype == 5) {
                            "当前为游客账户，如注销将删除游客账户及相关信息，是否继续？"
                        } else {
                            "是否退出登录？"
                        },
                        BUIDialog.BtnBuilder().withCancelBtn().withSubmitBtn {
                            UserManager.loginOut()
                            LoginActivity.startAction(activity!!, true)
                            BaseApplication.baseApplication.remove(MainActivity::class.java)
                            PushAgent.getInstance(activity!!.applicationContext)
                                .disable(object : IUmengCallback {
                                    override fun onSuccess() {
                                    }

                                    override fun onFailure(p0: String?, p1: String?) {
                                    }
                                })
                        })
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == 0x01) {
            menuList.removeAt(0)
            menuAdapter.notifyDataSetChanged()
        }
    }

    override fun onFeedBackResult() {
        showToast("提交成功，感谢您的反馈")
        dialog?.dismiss()
    }

    override fun userInfoUpdateResult(userBean: UserBean) {
        UserManager.user = userBean
        setUserInfo()
    }

    override fun onResume() {
        super.onResume()
        mPresenter.updateUserInfo(hashMapOf("appversion" to "Android_${ZXSystemUtil.getVersionName()}"))
    }
}
