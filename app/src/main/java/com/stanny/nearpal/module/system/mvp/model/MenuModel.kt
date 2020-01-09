package com.stanny.nearpal.module.system.mvp.model

import com.frame.zxmvp.base.BaseModel
import com.frame.zxmvp.baserx.RxHelper
import com.frame.zxmvp.baserx.RxSchedulers
import com.stanny.nearpal.app.ApiService
import com.stanny.nearpal.module.system.bean.UserBean

import com.stanny.nearpal.module.system.mvp.contract.MenuContract
import rx.Observable

/**
 * Create By admin On 2017/7/11
 * 功能：
 */
class MenuModel : BaseModel(), MenuContract.Model {
    override fun feedBackData(map: Map<String, String>): Observable<Any> {
        return mRepositoryManager.obtainRetrofitService(ApiService::class.java)
            .addFeedBack(map)
            .compose(RxHelper.handleResult())
            .compose(RxSchedulers.io_main())
    }

    override fun updateUserData(map: Map<String, String>): Observable<UserBean> {
        return mRepositoryManager.obtainRetrofitService(ApiService::class.java)
            .getMyInfo(map)
            .compose(RxHelper.handleResult())
            .compose(RxSchedulers.io_main())
    }


}