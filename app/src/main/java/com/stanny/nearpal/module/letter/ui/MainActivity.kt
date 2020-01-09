package com.stanny.nearpal.module.letter.ui

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import androidx.core.app.NotificationManagerCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.stanny.nearpal.R
import com.stanny.nearpal.base.BaseActivity
import com.stanny.nearpal.base.UserManager
import com.stanny.nearpal.module.letter.bean.LetterBean
import com.stanny.nearpal.module.letter.func.adapter.PenpalAdapter
import com.stanny.nearpal.module.letter.func.adapter.SaveLetterAdapter
import com.stanny.nearpal.module.letter.mvp.contract.MainContract
import com.stanny.nearpal.module.letter.mvp.model.MainModel
import com.stanny.nearpal.module.letter.mvp.presenter.MainPresenter
import com.stanny.nearpal.module.system.bean.UserBean
import com.stanny.nearpal.module.system.ui.MenuFragment
import com.umeng.message.IUmengCallback
import com.umeng.message.PushAgent
import com.zx.bui.ui.buidialog.BUIDialog
import com.zx.zxutils.util.ZXFragmentUtil
import com.zx.zxutils.util.ZXLogUtil
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
    private lateinit var menuFragment: MenuFragment

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
        PushAgent.getInstance(applicationContext).enable(object : IUmengCallback{
            override fun onSuccess() {
            }
            override fun onFailure(p0: String?, p1: String?) {
            }
        })
        PushAgent.getInstance(applicationContext).setAlias(UserManager.user!!.id.toString(), "id") { p0, p1 ->
            ZXLogUtil.loge("别名注册成功")
        }
    }

    /**
     * 初始化
     */
    override fun initView(savedInstanceState: Bundle?) {
        checkNotifyPermission()

        ZXFragmentUtil.addFragment(
                supportFragmentManager,
                LetterFragment.newInstance().apply { letterFragment = this },
                R.id.fm_letter
        )
        ZXFragmentUtil.addFragment(
                supportFragmentManager,
                MenuFragment.newInstance().apply { menuFragment = this },
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

        super.initView(savedInstanceState)
    }

    private fun checkNotifyPermission() {
        try {
            if (mSharedPrefUtil.contains("notifyPermissionCheck") && System.currentTimeMillis() - mSharedPrefUtil.getLong("notifyPermissionCheck") < 1000 * 60 * 60 * 24 * 2) {
                //两天内不再检测
                return
            }
            val notificationEnable = NotificationManagerCompat.from(this).areNotificationsEnabled()
            if (!notificationEnable) {
                mSharedPrefUtil.putLong("notifyPermissionCheck", System.currentTimeMillis())
                BUIDialog.showInfo(this,
                        "提示",
                        "未开启通知权限，需要实时接收来信通知（PS：我真没广告）",
                        BUIDialog.BtnBuilder()
                                .withCancelBtn()
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
            //如存在暂存信件，提示用户。
            if (mSharedPrefUtil.getList<LetterBean>("letterList")?.isNotEmpty() == true) {
                val saveLetter =
                        LayoutInflater.from(this).inflate(R.layout.layout_save_letter, null)
                val rvLetter = saveLetter.findViewById<RecyclerView>(R.id.rv_save_letter)
                val letterList = mSharedPrefUtil.getList<LetterBean>("letterList")
                val dialog = BUIDialog.showCustom(this, "存在暂存信件", saveLetter, BUIDialog.BtnBuilder()
                        .withCancelBtn()
                        .withBtn("继续新建信件", R.color.letter_blue) {
                            WriteLetterActivity.startAction(this, false)
                        })
                val saveLetterAdapter = SaveLetterAdapter(letterList)
                rvLetter.apply {
                    layoutManager = LinearLayoutManager(this@MainActivity)
                    adapter = saveLetterAdapter
                }
                saveLetterAdapter.setOnItemClickListener { adapter, view, position ->
                    WriteLetterActivity.startAction(
                            this,
                            false,
                            letterList[position].acceptuserid,
                            letterList[position].id
                    )
                    dialog.dismiss()
                }
                saveLetterAdapter.setOnItemChildClickListener { adapter, view, position ->
                    if (view.id == R.id.iv_save_delete) {
                        letterList.removeAt(position)
                        saveLetterAdapter.notifyDataSetChanged()
                        mSharedPrefUtil.putList("letterList", letterList)
                        if (letterList.isEmpty()) {
                            dialog.dismiss()
                            onResume()
                        }
                    }
                }
            } else {
                WriteLetterActivity.startAction(this, false)
            }
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
        if (mSharedPrefUtil.getList<LetterBean>("letterList")?.isNotEmpty() == true) {
            ttv_main_write.text = "书(续)"
        } else {
            ttv_main_write.text = "书"
        }
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        menuFragment.onActivityResult(requestCode, resultCode, data)
    }

}
