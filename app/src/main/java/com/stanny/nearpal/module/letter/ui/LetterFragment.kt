package com.stanny.nearpal.module.letter.ui

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.digitalcq.zhyqsjpt.base.BaseFragment
import com.stanny.nearpal.R
import com.stanny.nearpal.base.UserManager
import com.stanny.nearpal.module.letter.bean.LetterBean
import com.stanny.nearpal.module.letter.func.adapter.LetterAdapter
import com.stanny.nearpal.module.letter.func.tool.MyItemDecoration
import com.stanny.nearpal.module.letter.func.tool.MySnapHelper
import com.stanny.nearpal.module.letter.mvp.contract.LetterContract
import com.stanny.nearpal.module.letter.mvp.model.LetterModel
import com.stanny.nearpal.module.letter.mvp.presenter.LetterPresenter
import com.zx.bui.ui.buidialog.BUIDialog
import kotlinx.android.synthetic.main.fragment_letter.*


/**
 * Create By admin On 2017/7/11
 * 功能：
 */
class LetterFragment : BaseFragment<LetterPresenter, LetterModel>(), LetterContract.View {

    private val letterList = arrayListOf<LetterBean>()
    private val letterAdapter = LetterAdapter(letterList)

    private var targetPositon = 0

    private var reload: () -> Unit = {}

    companion object {
        /**
         * 启动器
         */
        fun newInstance(): LetterFragment {
            val fragment = LetterFragment()
            val bundle = Bundle()

            fragment.arguments = bundle
            return fragment
        }
    }

    /**
     * layout配置
     */
    override fun getLayoutId(): Int {
        return R.layout.fragment_letter
    }

    /**
     * 初始化
     */
    override fun initView(savedInstanceState: Bundle?) {
        //信件
        tv_letter_num.text = "0/0"

        rv_letter_list.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            adapter = letterAdapter
            addItemDecoration(MyItemDecoration(activity!!))
        }
        MySnapHelper.bindRecycler(activity!!, rv_letter_list) {
            targetPositon = it
            if (it < letterList.size) {
                tv_letter_num.text =
                    if (letterList.isEmpty()) "0/0" else (it + 1).toString() + "/" + letterList.size
            }
        }

        super.initView(savedInstanceState)
    }

    /**
     * View事件设置
     */
    override fun onViewListener() {
        letterAdapter.setOnItemClickListener { adapter, view, position ->
            if (letterList[position].mstatus != 3) {
                //如未拆封
                letterList[position].mstatus = 3
                letterAdapter.notifyItemChanged(position)
                resetNotRead()
                mPresenter.readLetter(hashMapOf("id" to letterList[position].id.toString()))
            }
            ReadLetterActivity.startAction(
                activity!!,
                false,
                letterList[position]
            )
        }
        letterAdapter.setOnItemLongClickListener { adapter, view, position ->
            BUIDialog.showInfo(activity!!, "提示", "是否删除该信件（仅对您不可见）？", BUIDialog.BtnBuilder()
                .withCancelBtn()
                .withSubmitBtn {
                    mPresenter.deleteLetter(hashMapOf("id" to letterList[position].id.toString()))
                })
            return@setOnItemLongClickListener false
        }
    }

    fun setDatas(letterList: List<LetterBean>) {
        this.letterList.clear()
        this.letterList.addAll(letterList)
        resetNotRead()

        tv_letter_num.text =
            if (letterList.isEmpty()) "0/0" else (targetPositon + 1).toString() + "/" + letterList.size
        letterAdapter.notifyDataSetChanged()
    }

    private fun resetNotRead() {
        var norreadNum = 0
        if (letterList.isNotEmpty()) {
            letterList.forEach {
                if (it.mstatus != 3 && it.senduserid != UserManager.user?.id) {
                    norreadNum++
                }
            }
        }
        tv_letter_notreadnum.text = "${norreadNum}封信件未拆>>"
    }

    fun setReloadCall(reload: () -> Unit) {
        this.reload = reload
    }

    override fun onDeleteResult() {
        showToast("删除成功")
//        letterList.removeAt(1)
//        letterAdapter.notifyDataSetChanged()
        reload()
    }
}
