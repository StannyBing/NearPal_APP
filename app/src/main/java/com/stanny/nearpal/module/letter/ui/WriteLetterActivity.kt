package com.stanny.nearpal.module.letter.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.stanny.nearpal.R
import com.stanny.nearpal.base.BaseActivity
import com.stanny.nearpal.base.setHead
import com.stanny.nearpal.base.toJson
import com.stanny.nearpal.module.letter.mvp.contract.WriteLetterContract
import com.stanny.nearpal.module.letter.mvp.model.WriteLetterModel
import com.stanny.nearpal.module.letter.mvp.presenter.WriteLetterPresenter
import com.stanny.nearpal.module.system.bean.UserBean
import com.zx.bui.ui.buidialog.BUIDialog
import kotlinx.android.synthetic.main.activity_write_letter.*


/**
 * Create By admin On 2017/7/11
 * 功能：
 */
class WriteLetterActivity : BaseActivity<WriteLetterPresenter, WriteLetterModel>(),
    WriteLetterContract.View {

    private var penpalInfo: UserBean? = null

    companion object {
        /**
         * 启动器
         */
        fun startAction(
            activity: Activity,
            isFinish: Boolean,
            sendUserId: Int? = null,
            letterId: Int? = null
        ) {
            val intent = Intent(activity, WriteLetterActivity::class.java)
            intent.putExtra("sendUserId", sendUserId)
            intent.putExtra("letterId", letterId)
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
        if (intent.getIntExtra("sendUserId", 0) != 0) {
            mPresenter.getUserInfo(
                hashMapOf(
                    "id" to intent.getIntExtra(
                        "sendUserId",
                        0
                    ).toString()
                )
            )
        }

        iv_write_letter_headicon1.setHead(penpalInfo?.headicon)
        iv_write_letter_headicon2.setHead(penpalInfo?.headicon)
        tv_write_letter_name.text = "随机信件"

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
            BUIDialog.showInfo(this, "提示", "是否立即发送", BUIDialog.BtnBuilder()
                .withCancelBtn()
                .withBtn("暂存") {

                }
                .withBtn("发送") {
                    mPresenter.sendLetter(
                        hashMapOf(
                            "lettercall" to et_write_penpal.text.toString().run {
                                if (isEmpty()) {
                                    "远方的你"
                                } else {
                                    this
                                }
                            },
                            "letterdetail" to tv_write_letter_content.text.toString(),
                            "stampid" to 1,
                            "acceptuserid" to penpalInfo?.id,
                            "replyletterid" to intent.getIntExtra("letterId", 0)
                        ).toJson()
                    )
                })
        }
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
}
