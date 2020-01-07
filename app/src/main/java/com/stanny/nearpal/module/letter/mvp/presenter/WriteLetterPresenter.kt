package com.stanny.nearpal.module.letter.mvp.presenter

import com.frame.zxmvp.baserx.RxHelper
import com.frame.zxmvp.baserx.RxSubscriber
import com.stanny.nearpal.module.letter.mvp.contract.WriteLetterContract
import com.stanny.nearpal.module.system.bean.UserBean
import okhttp3.RequestBody


/**
 * Create By admin On 2017/7/11
 * 功能：
 */
class WriteLetterPresenter : WriteLetterContract.Presenter() {
    override fun sendLetter(body: RequestBody) {
        mModel.letterSendData(body)
            .compose(RxHelper.bindToLifecycle(mView))
            .subscribe(object : RxSubscriber<Any>(mView) {
                override fun _onNext(t: Any?) {
                    mView.onLetterSendResult()
                }

                override fun _onError(code: Int, message: String?) {
                    mView.handleError(code, message)
                }
            })
    }

    override fun getUserInfo(map: Map<String, String>) {
        mModel.userdata(map)
            .compose(RxHelper.bindToLifecycle(mView))
            .subscribe(object : RxSubscriber<UserBean>() {
                override fun _onNext(t: UserBean?) {
                    if (t != null) {
                        mView.onUserResult(t)
                    }
                }

                override fun _onError(code: Int, message: String?) {
                    mView.handleError(code, message)
                }
            })
    }


}