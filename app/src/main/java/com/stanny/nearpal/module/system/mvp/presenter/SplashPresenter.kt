package com.stanny.nearpal.module.system.mvp.presenter

import com.frame.zxmvp.baserx.RxHelper
import com.frame.zxmvp.baserx.RxSubscriber
import com.stanny.nearpal.module.system.bean.UserBean
import com.stanny.nearpal.module.system.mvp.contract.SplashContract
import okhttp3.RequestBody


/**
 * Create By admin On 2017/7/11
 * 功能：
 */
class SplashPresenter : SplashContract.Presenter() {
    override fun loginApp(body: RequestBody) {
        mModel.loginData(body)
            .compose(RxHelper.bindToLifecycle(mView))
            .subscribe(object : RxSubscriber<UserBean>(mView) {
                override fun _onNext(t: UserBean?) {
                    mView.onLoginResult(t)
                }

                override fun _onError(code: Int, message: String?) {
                    mView.handleError(code, message)
                    mView.onLoginResult(null)
                }

            })
    }

    override fun visitLogin(body: RequestBody) {
        mModel.visitLoginData(body)
            .compose(RxHelper.bindToLifecycle(mView))
            .subscribe(object : RxSubscriber<UserBean>(mView) {
                override fun _onNext(t: UserBean?) {
                    mView.onLoginResult(t)
                }

                override fun _onError(code: Int, message: String?) {
                    mView.handleError(code, message)
                    mView.onLoginResult(null)
                }

            })
    }

}