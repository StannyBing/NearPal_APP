package com.stanny.nearpal.module.system.ui

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import cn.jpush.android.api.JPushInterface
import com.digitalcq.zhyqsjpt.base.BaseFragment
import com.frame.zxmvp.baseapp.BaseApplication
import com.stanny.nearpal.R
import com.stanny.nearpal.base.UserManager
import com.stanny.nearpal.base.setHead
import com.stanny.nearpal.module.letter.ui.MainActivity
import com.stanny.nearpal.module.system.func.MenuAdapter
import com.stanny.nearpal.module.system.mvp.contract.MenuContract
import com.stanny.nearpal.module.system.mvp.model.MenuModel
import com.stanny.nearpal.module.system.mvp.presenter.MenuPresenter
import com.zx.bui.ui.buidialog.BUIDialog
import kotlinx.android.synthetic.main.fragment_menu.*

/**
 * Create By admin On 2017/7/11
 * 功能：
 */
class MenuFragment : BaseFragment<MenuPresenter, MenuModel>(), MenuContract.View {

    private val menuList = arrayListOf<String>()
    private val menuAdapter = MenuAdapter(menuList)

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
        menuList.add("退出登录")
        rv_menu.apply {
            layoutManager = LinearLayoutManager(activity!!)
            adapter = menuAdapter
        }
        super.initView(savedInstanceState)
    }

    private fun setUserInfo() {
        if (UserManager.user?.headicon != null && UserManager.user?.headicon != 0) {
            iv_menu_headicon.setHead(UserManager.user!!.headicon)
        }
        tv_menu_nickname.text = UserManager.user?.nickname
    }

    /**
     * View事件设置
     */
    override fun onViewListener() {
        iv_menu_headicon.setOnClickListener {
            UserInfoActivity.startAction(activity!!, false, true)
        }
        menuAdapter.setOnItemClickListener { adapter, view, position ->
            when (menuList[position]) {
                "当前为游客登录，请及时注册" -> {
                    RegisterActivity.startAction(activity!!, false)
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
                            JPushInterface.stopPush(activity!!)
                        })
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        setUserInfo()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == 0x01) {
            menuList.removeAt(0)
            menuAdapter.notifyDataSetChanged()
        }
    }
}
