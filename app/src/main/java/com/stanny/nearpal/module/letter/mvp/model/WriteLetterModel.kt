package com.stanny.nearpal.module.letter.mvp.model

import com.frame.zxmvp.base.BaseModel
import com.frame.zxmvp.baserx.RxHelper
import com.frame.zxmvp.baserx.RxSchedulers
import com.stanny.nearpal.app.ApiService

import com.stanny.nearpal.module.letter.mvp.contract.WriteLetterContract
import com.stanny.nearpal.module.system.bean.UserBean
import okhttp3.RequestBody
import rx.Observable

/**
 * Create By admin On 2017/7/11
 * 功能：
 */
class WriteLetterModel : BaseModel(), WriteLetterContract.Model {
    override fun letterSendData(body: RequestBody): Observable<Any> {
        return mRepositoryManager.obtainRetrofitService(ApiService::class.java)
            .sendLetter(body)
            .compose(RxHelper.handleResult())
            .compose(RxSchedulers.io_main())
    }
    override fun userdata(map: Map<String, String>): Observable<UserBean> {
        return mRepositoryManager.obtainRetrofitService(ApiService::class.java)
            .getUserInfo(map)
            .compose(RxHelper.handleResult())
            .compose(RxSchedulers.io_main())
    }

}