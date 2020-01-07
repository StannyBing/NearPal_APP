package com.stanny.nearpal.module.system.mvp.model

import com.frame.zxmvp.base.BaseModel
import com.frame.zxmvp.baserx.RxHelper
import com.frame.zxmvp.baserx.RxSchedulers
import com.stanny.nearpal.app.ApiService
import com.stanny.nearpal.module.system.bean.UserBean

import com.stanny.nearpal.module.system.mvp.contract.UserInfoContract
import okhttp3.RequestBody
import rx.Observable

/**
 * Create By admin On 2017/7/11
 * 功能：
 */
class UserInfoModel : BaseModel(), UserInfoContract.Model {
    override fun modifyData(body : RequestBody): Observable<UserBean> {
        return mRepositoryManager.obtainRetrofitService(ApiService::class.java)
                .modifyUserInfo(body)
                .compose(RxHelper.handleResult())
                .compose(RxSchedulers.io_main())
    }


}