package com.stanny.nearpal.module.letter.ui

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.core.app.NotificationManagerCompat
import androidx.recyclerview.widget.LinearLayoutManager
import cn.jpush.android.api.JPushInterface
import com.stanny.nearpal.BuildConfig
import com.stanny.nearpal.R
import com.stanny.nearpal.base.BaseActivity
import com.stanny.nearpal.base.UserManager
import com.stanny.nearpal.module.letter.bean.LetterBean
import com.stanny.nearpal.module.letter.func.adapter.PenpalAdapter
import com.stanny.nearpal.module.letter.mvp.contract.MainContract
import com.stanny.nearpal.module.letter.mvp.model.MainModel
import com.stanny.nearpal.module.letter.mvp.presenter.MainPresenter
import com.stanny.nearpal.module.system.bean.UserBean
import com.stanny.nearpal.module.system.ui.MenuFragment
import com.tencent.bugly.crashreport.CrashReport
import com.zx.bui.ui.buidialog.BUIDialog
import com.zx.zxutils.util.ZXFragmentUtil
import com.zx.zxutils.views.SlidingLayout.ZXSlidingRootNav
import com.zx.zxutils.views.SlidingLayout.ZXSlidingRootNavBuilder
import kotlinx.android.synthetic.main.activity_main.*


/**
 * Create By admin On 2017/7/11
 * 功能：
 */
class MainActivity : BaseActivity<MainPresenter, MainModel>(), MainContract.View {

    private val penpalList = arrayListOf<UserBean>()
    private val penpalAdapter = PenpalAdapter(penpalList)

    private var slidingBuilder: ZXSlidingRootNav? = null//侧边栏

    private lateinit var letterFragment: LetterFragment

    companion object {
        /**
         * 启动器
         */
        fun startAction(activity: Activity, isFinish: Boolean) {
            val intent = Intent(activity, MainActivity::class.java)
            activity.startActivity(intent)
            if (isFinish) activity.finish()
        }
    }

    /**
     * layout配置
     */
    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        JPushInterface.resumePush(this)
        JPushInterface.setAlias(this, 0, UserManager.user!!.id.toString())
    }

    /**
     * 初始化
     */
    override fun initView(savedInstanceState: Bundle?) {
        try {
            val notificationEnable = NotificationManagerCompat.from(this).areNotificationsEnabled()
            if (!notificationEnable) {
                BUIDialog.showInfo(this,
                    "提示",
                    "未开启通知权限，需要实时接收来信通知（PS：我真没广告）",
                    BUIDialog.BtnBuilder().withCancelBtn()
                        .withSubmitBtn {
                            val intent = Intent().apply {
                                if (Build.VERSION.SDK_INT >= 9) {
                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                    action = "android.settings.APPLICATION_DETAILS_SETTINGS"
                                    data = Uri.fromParts("package", packageName, null)
                                } else {
                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                    action = Intent.ACTION_VIEW
                                    setClassName(
                                        "com.android.settings",
                                        "com.android.settings.InstalledAppDetails"
                                    )
                                    putExtra("com.android.settings.ApplicationPkgName", packageName)
                                }
                            }
                            this@MainActivity.startActivity(intent)
                        })
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        ZXFragmentUtil.addFragment(
            supportFragmentManager,
            LetterFragment.newInstance().apply { letterFragment = this },
            R.id.fm_letter
        )
        ZXFragmentUtil.addFragment(
            supportFragmentManager,
            MenuFragment.newInstance(),
            R.id.fl_menu
        )

        //初始化侧边菜单
        slidingBuilder = ZXSlidingRootNavBuilder(this)
            .withSavedState(savedInstanceState)
            .withMenuLayout(R.layout.layout_main_menu)
            .withMenuOpened(false)
            .build()

        //笔友
        rv_main_penpal.apply {
            layoutManager =
                LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = penpalAdapter
        }

        try {
            if (BuildConfig.ISRELEASE) {
                CrashReport.setUserId(UserManager.user!!.id.toString())
            }
            if (mSharedPrefUtil.getBool("isGetPush")) {
                val bundle = Bundle()
                val map = mSharedPrefUtil.getMap<String, String>("jPushBundle")
                bundle.putString(
                    JPushInterface.EXTRA_NOTIFICATION_TITLE,
                    map.get(JPushInterface.EXTRA_NOTIFICATION_TITLE)
                )
                bundle.putString(JPushInterface.EXTRA_ALERT, map.get(JPushInterface.EXTRA_ALERT))
                bundle.putString(JPushInterface.EXTRA_EXTRA, map.get(JPushInterface.EXTRA_EXTRA))
                mRxManager.post("jPush", bundle)
            }
        } catch (e: Exception) {
        }

        super.initView(savedInstanceState)
    }

    fun loadData() {
        mPresenter.getAcceptLetters()
        mPresenter.getPenpalList()
    }

    /**
     * View事件设置
     */
    override fun onViewListener() {
        penpalAdapter.setOnItemClickListener { adapter, view, position ->
            PenpalDetailActivity.startAction(this@MainActivity, false, penpalList[position].id)
        }
        penpalAdapter.setOnItemLongClickListener { adapter, view, position ->
            showToast("删除")
            return@setOnItemLongClickListener true
        }
        ttv_main_write.setOnClickListener {
            WriteLetterActivity.startAction(this@MainActivity, false)
        }
        iv_main_menu.setOnClickListener {
            slidingBuilder?.openMenu(true)
        }
        letterFragment.setReloadCall {
            loadData()
        }
    }

    /**
     * 判断当前activity是否在栈顶，避免重复处理
     */
    private fun isTopActivity(): Boolean {
        var isTop = false
        val am = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val cn = am.getRunningTasks(1)[0].topActivity
        if (cn.className.contains(this.localClassName)) {
            isTop = true
        }
        return isTop
    }

    override fun onBackPressed() {
        moveTaskToBack(true)
    }

    override fun onResume() {
        super.onResume()
        loadData()
    }

    /**
     * 信件列表
     */
    override fun onLetterListResult(letterList: List<LetterBean>) {
        letterFragment.setDatas(letterList)
    }

    /**
     * 笔友列表
     */
    override fun onPenpalListResult(userList: List<UserBean>) {
        this.penpalList.clear()
        this.penpalList.addAll(userList)
        penpalAdapter.notifyDataSetChanged()
    }

}
