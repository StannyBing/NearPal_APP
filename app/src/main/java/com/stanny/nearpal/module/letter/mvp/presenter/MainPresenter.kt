package com.stanny.nearpal.module.letter.mvp.presenter

import com.frame.zxmvp.baserx.RxHelper
import com.frame.zxmvp.baserx.RxSubscriber
import com.stanny.nearpal.module.letter.bean.LetterBean
import com.stanny.nearpal.module.letter.mvp.contract.MainContract
import com.stanny.nearpal.module.system.bean.UserBean


/**
 * Create By admin On 2017/7/11
 * 功能：
 */
class MainPresenter : MainContract.Presenter() {
    override fun getAcceptLetters() {
        mModel.letterListData()
            .compose(RxHelper.bindToLifecycle(mView))
            .subscribe(object : RxSubscriber<List<LetterBean>>() {
                override fun _onNext(t: List<LetterBean>?) {
                    if (t != null) {
                        mView.onLetterListResult(t)
                    }
                }

                override fun _onError(code: Int, message: String?) {
                    mView.handleError(code, message)
                }
            })
    }

    override fun getPenpalList() {
        mModel.penpalListData()
            .compose(RxHelper.bindToLifecycle(mView))
            .subscribe(object : RxSubscriber<List<UserBean>>() {
                override fun _onNext(t: List<UserBean>?) {
                    if (t != null) {
                        mView.onPenpalListResult(t)
                    }
                }

                override fun _onError(code: Int, message: String?) {
                    mView.handleError(code, message)
                }

            })
    }



}