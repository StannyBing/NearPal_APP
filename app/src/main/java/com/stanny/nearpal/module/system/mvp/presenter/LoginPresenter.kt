package com.stanny.nearpal.module.system.mvp.presenter

import com.frame.zxmvp.baserx.RxHelper
import com.frame.zxmvp.baserx.RxSubscriber
import com.stanny.nearpal.module.system.bean.UserBean
import com.stanny.nearpal.module.system.mvp.contract.LoginContract
import okhttp3.RequestBody


/**
 * Create By admin On 2017/7/11
 * 功能：
 */
class LoginPresenter : LoginContract.Presenter() {
    override fun loginApp(body: RequestBody) {
        mModel.loginData(body)
            .compose(RxHelper.bindToLifecycle(mView))
            .subscribe(object : RxSubscriber<UserBean>(mView) {
                override fun _onNext(t: UserBean?) {
                    if (t != null) {
                        mView.onLoginResult(t)
                    }
                }

                override fun _onError(code: Int, message: String?) {
                    mView.handleError(code, message)
                }

            })
    }

    override fun visitRegister() {
        mModel.visitRegisterData()
            .compose(RxHelper.bindToLifecycle(mView))
            .subscribe(object : RxSubscriber<UserBean>(mView) {
                override fun _onNext(t: UserBean?) {
                    if (t != null) {
                        mView.onVisitRegisterResult(t)
                    }
                }

                override fun _onError(code: Int, message: String?) {
                    mView.handleError(code, message)
                }

            })
    }


}