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
import com.stanny.nearpal.module.letter.bean.LetterBean
import com.stanny.nearpal.module.letter.mvp.contract.ReadLetterContract
import com.stanny.nearpal.module.letter.mvp.model.ReadLetterModel
import com.stanny.nearpal.module.letter.mvp.presenter.ReadLetterPresenter
import kotlinx.android.synthetic.main.activity_read_letter.*


/**
 * Create By admin On 2017/7/11
 * 功能：
 */
class ReadLetterActivity : BaseActivity<ReadLetterPresenter, ReadLetterModel>(),
    ReadLetterContract.View {

    private lateinit var letterBean: LetterBean

    companion object {
        /**
         * 启动器
         */
        fun startAction(activity: Activity, isFinish: Boolean, letterBean: LetterBean?) {
            val intent = Intent(activity, ReadLetterActivity::class.java)
            intent.putExtra("letterInfo", letterBean)
            activity.startActivity(intent)
            if (isFinish) activity.finish()
        }
    }

    /**
     * layout配置
     */
    override fun getLayoutId(): Int {
        return R.layout.activity_read_letter
    }

    /**
     * 初始化
     */
    override fun initView(savedInstanceState: Bundle?) {
        letterBean = intent.getSerializableExtra("letterInfo") as LetterBean
        tv_read_letter_name.text = letterBean.nickname
        iv_read_letter_headicon1.setHead(letterBean.headicon)
        iv_read_letter_headicon2.setHead(letterBean.headicon)

        tv_letter_call.text = letterBean.lettercall
        tv_read_letter_content.setText(letterBean.letterdetail)

        if (letterBean.senduserid == UserManager.user?.id) {
            ttv_read_write.visibility = View.GONE
        }

        super.initView(savedInstanceState)
    }

    /**
     * View事件设置
     */
    @SuppressLint("NewApi")
    override fun onViewListener() {
        //监听滚动事件，设置图标可见性
        sv_read_letter_scroll.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            val top = (iv_read_letter_headicon2.getParent() as View).top
            val height: Int = iv_read_letter_headicon2.getHeight()
            if (scrollY - top < 0) {
                iv_read_letter_headicon1.setAlpha(0.0f)
            } else if (scrollY - top > height) {
                iv_read_letter_headicon1.setAlpha(1.0f)
            } else {
                iv_read_letter_headicon1.setAlpha((scrollY - top) / height.toFloat())
            }
        }
        iv_read_letter_close.setOnClickListener { finish() }

        iv_read_letter_headicon2.setOnClickListener {
            if (letterBean.senduserid != UserManager.user?.id) {
                PenpalDetailActivity.startAction(
                    this,
                    false,
                    letterBean.senduserid
                )
            }
        }
        tv_read_letter_name.setOnClickListener {
            if (letterBean.senduserid != UserManager.user?.id) {
                PenpalDetailActivity.startAction(
                    this,
                    false,
                    letterBean.senduserid
                )
            }
        }
        ttv_read_write.setOnClickListener {
            if (letterBean.senduserid != 1) {
                WriteLetterActivity.startAction(this, true, letterBean.senduserid, letterBean.id)
            } else {
                WriteLetterActivity.startAction(this, true)
            }
        }
    }

}
