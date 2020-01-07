package com.stanny.nearpal.module.letter.mvp.presenter

import com.frame.zxmvp.baserx.RxHelper
import com.frame.zxmvp.baserx.RxSubscriber
import com.stanny.nearpal.module.letter.bean.LetterBean
import com.stanny.nearpal.module.letter.mvp.contract.PenpalDetailContract


/**
 * Create By admin On 2017/7/11
 * 功能：
 */
class PenpalDetailPresenter : PenpalDetailContract.Presenter() {
    override fun getLetterWith(id: Int) {
        mModel.userdata(hashMapOf("id" to id.toString()))
            .compose(RxHelper.bindToLifecycle(mView))
            .flatMap {
                mView.onUserResult(it)
                return@flatMap mModel.lettersData(hashMapOf("userid" to id.toString()))
            }
            .subscribe(object : RxSubscriber<List<LetterBean>>() {
                override fun _onNext(t: List<LetterBean>?) {
                    if (t != null) {
                        mView.onLettersResult(t)
                    }
                }

                override fun _onError(code: Int, message: String?) {
                    mView.handleError(code, message)
                }

            })
    }

    override fun deletePenpal(map: Map<String, String>) {
        mModel.deletePenpal(map)
            .compose(RxHelper.bindToLifecycle(mView))
            .subscribe(object : RxSubscriber<Any>(mView) {
                override fun _onNext(t: Any?) {
                    mView.onPenPalDelete()
                }

                override fun _onError(code: Int, message: String?) {
                    mView.handleError(code, message)
                }
            })
    }


}