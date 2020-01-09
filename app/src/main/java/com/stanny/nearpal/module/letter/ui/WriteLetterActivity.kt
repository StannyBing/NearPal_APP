package com.stanny.nearpal.module.letter.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.stanny.nearpal.R
import com.stanny.nearpal.base.BaseActivity
import com.stanny.nearpal.base.UserManager
import com.stanny.nearpal.base.setHead
import com.stanny.nearpal.base.toJson
import com.stanny.nearpal.module.letter.bean.LetterBean
import com.stanny.nearpal.module.letter.mvp.contract.WriteLetterContract
import com.stanny.nearpal.module.letter.mvp.model.WriteLetterModel
import com.stanny.nearpal.module.letter.mvp.presenter.WriteLetterPresenter
import com.stanny.nearpal.module.system.bean.UserBean
import com.zx.bui.ui.buidialog.BUIDialog
import com.zx.zxutils.util.ZXTimeUtil
import kotlinx.android.synthetic.main.activity_write_letter.*
import rx.Observable
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.util.concurrent.TimeUnit


/**
 * Create By admin On 2017/7/11
 * 功能：
 */
class WriteLetterActivity : BaseActivity<WriteLetterPresenter, WriteLetterModel>(),
        WriteLetterContract.View {

    private var penpalInfo: UserBean? = null

    private var tempLetter: LetterBean? = null

    private var acceptUserId: Int? = 0
    private var replyLetterId = 0

    private var observable: Subscription? = null

    companion object {
        /**
         * 启动器
         */
        fun startAction(activity: Activity, isFinish: Boolean, acceptUserId: Int? = null, replyLetterId: Int? = null) {
            val intent = Intent(activity, WriteLetterActivity::class.java)
            intent.putExtra("acceptUserId", acceptUserId)
            intent.putExtra("replyLetterId", replyLetterId)
            activity.startActivityForResult(intent, 0x01)
            if (isFinish) activity.finish()
        }

    }

    /**
     * layout配置
     */
    override fun getLayoutId(): Int {
        return R.layout.activity_write_letter
    }

    /**
     * 初始化
     */
    override fun initView(savedInstanceState: Bundle?) {

        replyLetterId = intent.getIntExtra("replyLetterId", 0)
        acceptUserId = intent.getIntExtra("acceptUserId", 0)
        if (acceptUserId == 0) acceptUserId = null

        val letterList = arrayListOf<LetterBean>()
        if (mSharedPrefUtil.getList<LetterBean>("letterList")?.isNotEmpty() == true) {
            letterList.addAll(mSharedPrefUtil.getList("letterList"))
        }
        //自动从数据库中取出暂存数据
        if (letterList.isNotEmpty()) {
            letterList.forEach {
                if (it.acceptuserid == acceptUserId) {
                    acceptUserId = it.acceptuserid
                    et_write_call.setText(it.lettercall)
                    et_write_letter_content.setText(it.letterdetail)
                    return@forEach
                }
            }
        }
        if (acceptUserId != null && acceptUserId != 0) {
            mPresenter.getUserInfo(hashMapOf("id" to acceptUserId.toString()))
        }


        iv_write_letter_headicon1.setHead(penpalInfo?.headicon)
        iv_write_letter_headicon2.setHead(penpalInfo?.headicon)
        tv_write_letter_name.text = "随机信件"

        //每2分钟保存一次
        observable = Observable.interval(1L, 2L, TimeUnit.MINUTES)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    if (et_write_letter_content.text.toString().isNotEmpty()) {
                        saveLetter()
                        showToast("自动保存成功")
                    }
                }

        super.initView(savedInstanceState)
    }


    /**
     * View事件设置
     */
    @SuppressLint("NewApi")
    override fun onViewListener() {
        //监听滚动事件，设置图标可见性
        sv_write_letter_scroll.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            val top = (iv_write_letter_headicon2.getParent() as View).top
            val height: Int = iv_write_letter_headicon2.getHeight()
            if (scrollY - top < 0) {
                iv_write_letter_headicon1.setAlpha(0.0f)
            } else if (scrollY - top > height) {
                iv_write_letter_headicon1.setAlpha(1.0f)
            } else {
                iv_write_letter_headicon1.setAlpha((scrollY - top) / height.toFloat())
            }
        }
        iv_write_letter_close.setOnClickListener { finish() }

        ttv_write_send.setOnClickListener {
            BUIDialog.showInfo(this, "提示", "是否立即发送（-1楮豆豆）", BUIDialog.BtnBuilder()
                    .withCancelBtn()
                    .withBtn("暂存") {
                        saveLetter()
                        showToast("保存成功，您可随时进行继续书写")
                        finish()
                    }
                    .withBtn("发送") {
                        it.dismiss()
                        if (UserManager.user?.balance!! == 0) {
                            BUIDialog.showInfo(this, "提示", "褚豆豆不足，目前暂未开放充值，请通过意见反馈或微信公众号与我联系")
                            return@withBtn
                        }
                        mPresenter.sendLetter(
                                hashMapOf(
                                        "lettercall" to et_write_call.text.toString().run {
                                            if (isEmpty()) {
                                                "远方的你"
                                            } else {
                                                this
                                            }
                                        },
                                        "letterdetail" to et_write_letter_content.text.toString(),
                                        "stampid" to 1,
                                        "acceptuserid" to penpalInfo?.id,
                                        "replyletterid" to replyLetterId
                                ).toJson()
                        )
                        val letterList = arrayListOf<LetterBean>()
                        if (mSharedPrefUtil.getList<LetterBean>("letterList")?.isNotEmpty() == true) {
                            letterList.addAll(mSharedPrefUtil.getList("letterList"))
                        }
                        if (letterList.isNotEmpty()) {
                            letterList.forEach {
                                if (it.acceptuserid == acceptUserId) {
                                    letterList.remove(it)
                                    mSharedPrefUtil.putList("letterList", letterList)
                                    return@forEach
                                }
                            }
                        }
                    })
        }
    }

    private fun saveLetter() {
        val letterList = arrayListOf<LetterBean>()
        if (mSharedPrefUtil.getList<LetterBean>("letterList")?.isNotEmpty() == true) {
            letterList.addAll(mSharedPrefUtil.getList("letterList"))
        }
        if (letterList.isNotEmpty()) {
            letterList.forEach {
                if (it.acceptuserid == acceptUserId) {
                    tempLetter = it
                    setInfo()
                    mSharedPrefUtil.putList("letterList", letterList)
                    return
                }
            }
        }
        tempLetter = LetterBean()
        setInfo()
        letterList.add(tempLetter!!)
        mSharedPrefUtil.putList("letterList", letterList)
    }

    private fun setInfo() {
        tempLetter?.id = replyLetterId
        tempLetter?.lettercall = et_write_call.text.toString().run {
            if (isEmpty()) {
                "远方的你"
            } else {
                this
            }
        }
        tempLetter?.sendtime = ZXTimeUtil.getCurrentTime()
        tempLetter?.letterdetail = et_write_letter_content.text.toString()
        tempLetter?.acceptuserid = penpalInfo?.id
        tempLetter?.headicon = penpalInfo?.headicon
        tempLetter?.nickname = penpalInfo?.nickname
    }

    override fun onUserResult(userBean: UserBean) {
        this.penpalInfo = userBean
        tv_write_letter_name.text = penpalInfo?.nickname
        iv_write_letter_headicon1.setHead(penpalInfo?.headicon)
        iv_write_letter_headicon2.setHead(penpalInfo?.headicon)
    }

    override fun onLetterSendResult() {
        showToast("发送成功")
        setResult(0x01)
        finish()
    }

    override fun onStop() {
        observable?.unsubscribe()
        super.onStop()
    }

    override fun onDestroy() {
        observable?.unsubscribe()
        super.onDestroy()
    }
}
