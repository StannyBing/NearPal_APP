package com.stanny.nearpal.module.system.mvp.presenter

import com.frame.zxmvp.baserx.RxHelper
import com.frame.zxmvp.baserx.RxSubscriber
import com.stanny.nearpal.module.system.bean.UserBean
import com.stanny.nearpal.module.system.mvp.contract.RegisterContract
import okhttp3.RequestBody


/**
 * Create By admin On 2017/7/11
 * 功能：
 */
class RegisterPresenter : RegisterContract.Presenter() {
    override fun doRegister(body: RequestBody) {
        mModel.registerData(body)
            .compose(RxHelper.bindToLifecycle(mView))
            .subscribe(object : RxSubscriber<UserBean>(mView) {
                override fun _onNext(t: UserBean?) {
                    mView.onRegisterResult(t)
                }

                override fun _onError(code: Int, message: String?) {
                    mView.handleError(code, message)
                    mView.onRegisterResult(null)
                }
            })
    }


}