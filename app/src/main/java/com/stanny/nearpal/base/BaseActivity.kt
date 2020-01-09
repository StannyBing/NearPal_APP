package com.stanny.nearpal.base

import android.Manifest
import android.app.ActivityManager
import android.content.Context
import android.os.Bundle
import android.os.Handler
import com.frame.zxmvp.base.BaseModel
import com.frame.zxmvp.base.BasePresenter
import com.frame.zxmvp.base.RxBaseActivity
import com.stanny.nearpal.BuildConfig
import com.stanny.nearpal.app.ApiConfigModule
import com.stanny.nearpal.module.letter.ui.MainActivity
import com.stanny.nearpal.module.system.ui.LoginActivity
import com.stanny.nearpal.module.system.ui.RegisterActivity
import com.stanny.nearpal.module.system.ui.SplashActivity
import com.umeng.analytics.MobclickAgent
import com.umeng.message.PushAgent
import com.zx.bui.ui.buidialog.BUIDialog
import com.zx.zxutils.util.*
import com.zx.zxutils.views.ZXStatusBarCompat
import rx.functions.Action1


/**
 * Created by Xiangb on 2019/2/26.
 * 功能：
 */
abstract class BaseActivity<T : BasePresenter<*, *>, E : BaseModel> : RxBaseActivity<T, E>() {
    var pageSize = 15
    val mSharedPrefUtil = ZXSharedPrefUtil()
    val handler = Handler()
    var sIsLoginClear = false

    private var loadMills = 0L

    private var permessionBack: () -> Unit = {}
    private var permissionArray: Array<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ZXStatusBarCompat.setStatusBarLightMode(this)
        ZXCrashUtil.init(BuildConfig.ISRELEASE) { t, e ->
            showToast("出现未知问题，请稍后再试")
        }
        PushAgent.getInstance(this).onAppStart()

        mRxManager.on("jPush", Action1<Bundle> {
            try {
                if (this is MainActivity){
                    this.loadData()
                    return@Action1
                }else if (isTopActivity() || this is SplashActivity || this is LoginActivity || this is RegisterActivity) {
                    return@Action1
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        })
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

    override fun showToast(message: String) {
        ZXToastUtil.showToast(message)
    }

    override fun showLoading(message: String) {
        loadMills = System.currentTimeMillis()
        BUIDialog.showLoading(this, message)
        handler.postDelayed({
            if (System.currentTimeMillis() - loadMills >= 5000) {
                dismissLoading()
            }
        }, 5000)
    }

    override fun dismissLoading() {
        BUIDialog.dismissLoading()
    }


    override fun showLoading(message: String, progress: Int) {
        try {
            BUIDialog.showLoading(this, message, progress)
        } catch (e: Exception) {
        }
    }

    override fun handleError(code: Int, message: String) {
        showToast(message)
        if (code == 10002) {
            ApiConfigModule.COOKIE = ""
            SplashActivity.startAction(this, false)
        }
    }

    override fun initView(savedInstanceState: Bundle?) {
        onViewListener()
    }

    abstract fun onViewListener()

    fun getPermission(permissionArray: Array<String>, permessionBack: () -> Unit) {
        this.permessionBack = permessionBack
        this.permissionArray = permissionArray
        if (permissionArray.contains(Manifest.permission.ACCESS_COARSE_LOCATION)) {
            if (!ZXLocationUtil.isLocationEnabled()) {
                ZXLocationUtil.openGPS(this)
                return
            }
        }
        if (!ZXPermissionUtil.checkPermissionsByArray(permissionArray)) {
            ZXPermissionUtil.requestPermissionsByArray(this)
        } else {
            this.permessionBack()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (permissionArray == null) {
            return
        }
        if (ZXPermissionUtil.checkPermissionsByArray(permissionArray)) {
            permessionBack()
        } else {
            showToast("未获取到系统权限，请先在设置中开启相应权限！")
        }
    }

    override fun onResume() {
        super.onResume()
        MobclickAgent.onResume(this)
    }

    override fun onPause() {
        super.onPause()
        MobclickAgent.onPause(this)
    }

}