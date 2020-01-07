package com.stanny.nearpal.module.letter.mvp.presenter

import com.frame.zxmvp.baserx.RxHelper
import com.frame.zxmvp.baserx.RxSubscriber
import com.stanny.nearpal.module.letter.mvp.contract.LetterContract


/**
 * Create By admin On 2017/7/11
 * 功能：
 */
class LetterPresenter : LetterContract.Presenter() {
    override fun readLetter(map: Map<String, String>) {
        mModel.readLetterData(map)
            .compose(RxHelper.bindToLifecycle(mView))
            .subscribe(object : RxSubscriber<Any>() {
                override fun _onNext(t: Any?) {
                }

                override fun _onError(code: Int, message: String?) {
                }
            })
    }

    override fun deleteLetter(map: Map<String, String>) {
        mModel.deleteLetterData(map)
            .compose(RxHelper.bindToLifecycle(mView))
            .subscribe(object : RxSubscriber<Any>(mView) {
                override fun _onNext(t: Any?) {
                    mView.onDeleteResult()
                }

                override fun _onError(code: Int, message: String?) {
                    mView.handleError(code, message)
                }

            })
    }


}