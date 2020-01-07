package com.stanny.nearpal.module.letter.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.stanny.nearpal.R
import com.stanny.nearpal.base.BaseActivity
import com.stanny.nearpal.base.setHead
import com.stanny.nearpal.module.letter.bean.LetterBean
import com.stanny.nearpal.module.letter.mvp.contract.PenpalDetailContract
import com.stanny.nearpal.module.letter.mvp.model.PenpalDetailModel
import com.stanny.nearpal.module.letter.mvp.presenter.PenpalDetailPresenter
import com.stanny.nearpal.module.system.bean.UserBean
import com.zx.bui.ui.buidialog.BUIDialog
import com.zx.zxutils.util.ZXFragmentUtil
import kotlinx.android.synthetic.main.activity_penpal_detail.*


/**
 * Create By admin On 2017/7/11
 * 功能：
 */
class PenpalDetailActivity : BaseActivity<PenpalDetailPresenter, PenpalDetailModel>(),
    PenpalDetailContract.View {

    private lateinit var letterFragment: LetterFragment

    private var userBean: UserBean? = null

    companion object {
        /**
         * 启动器
         */
        fun startAction(activity: Activity, isFinish: Boolean, userid: Int) {
            val intent = Intent(activity, PenpalDetailActivity::class.java)
            intent.putExtra("userid", userid)
            activity.startActivity(intent)
            if (isFinish) activity.finish()
        }
    }

    /**
     * layout配置
     */
    override fun getLayoutId(): Int {
        return R.layout.activity_penpal_detail
    }

    /**
     * 初始化
     */
    override fun initView(savedInstanceState: Bundle?) {
        ZXFragmentUtil.addFragment(
            supportFragmentManager,
            LetterFragment.newInstance().apply { letterFragment = this },
            R.id.fm_letter
        )

        super.initView(savedInstanceState)
    }

    private fun loadData() {
        mPresenter.getLetterWith(intent.getIntExtra("userid", 0))
    }

    /**
     * View事件设置
     */
    override fun onViewListener() {
        iv_penpalDetail_close.setOnClickListener { finish() }

        ttv_penpal_forget.setOnClickListener {
            BUIDialog.showInfo(this,
                "提示",
                "是否要遗忘掉TA，一旦选择遗忘，将删除掉与TA相关的所有信件！缘分来得好不容易，请谨慎决定！",
                BUIDialog.BtnBuilder()
                    .withCancelBtn()
                    .withSubmitBtn {
                        mPresenter.deletePenpal(hashMapOf("penpalid" to userBean?.id.toString()))
                    })
        }

        ttv_penpal_write.setOnClickListener {
            WriteLetterActivity.startAction(this, false, userBean?.id)
        }

        letterFragment.setReloadCall {
            loadData()
        }
    }

    override fun onUserResult(userBean: UserBean) {
        this.userBean = userBean
        iv_penpalDetail_headicon.setHead(userBean.headicon)
        tv_penpalDetail_nickname.text = userBean.nickname
    }

    override fun onLettersResult(letterList: List<LetterBean>) {
        letterFragment.setDatas(letterList)
    }

    override fun onResume() {
        super.onResume()
        loadData()
    }

    override fun onPenPalDelete() {
        showToast("操作成功...>_<")
        finish()
    }

}
