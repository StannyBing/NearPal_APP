package com.stanny.nearpal.module.system.mvp.presenter

import com.frame.zxmvp.baserx.RxHelper
import com.frame.zxmvp.baserx.RxSubscriber
import com.stanny.nearpal.module.system.bean.UserBean
import com.stanny.nearpal.module.system.mvp.contract.MenuContract


/**
 * Create By admin On 2017/7/11
 * 功能：
 */
class MenuPresenter : MenuContract.Presenter() {
    override fun sendFeedBack(map: Map<String, String>) {
        mModel.feedBackData(map)
            .compose(RxHelper.bindToLifecycle(mView))
            .subscribe(object : RxSubscriber<Any>(mView) {
                override fun _onNext(t: Any?) {
                    mView.onFeedBackResult()
                }

                override fun _onError(code: Int, message: String?) {
                    mView.handleError(code, message)
                }

            })
    }

    override fun updateUserInfo(map: Map<String, String>) {
        mModel.updateUserData(map)
            .compose(RxHelper.bindToLifecycle(mView))
            .subscribe(object : RxSubscriber<UserBean>() {
                override fun _onNext(t: UserBean?) {
                    if (t != null) {
                        mView.userInfoUpdateResult(t)
                    }
                }

                override fun _onError(code: Int, message: String?) {
                    mView.handleError(code, message)
                }

            })
    }


}