package com.stanny.nearpal.module.letter.mvp.model

import com.frame.zxmvp.base.BaseModel
import com.frame.zxmvp.baserx.RxHelper
import com.frame.zxmvp.baserx.RxSchedulers
import com.stanny.nearpal.app.ApiService
import com.stanny.nearpal.module.letter.bean.LetterBean
import com.stanny.nearpal.module.letter.mvp.contract.MainContract
import com.stanny.nearpal.module.system.bean.UserBean
import rx.Observable

/**
 * Create By admin On 2017/7/11
 * 功能：
 */
class MainModel : BaseModel(), MainContract.Model {
    override fun letterListData(): Observable<List<LetterBean>> {
        return mRepositoryManager.obtainRetrofitService(ApiService::class.java)
            .getAcceptLetters()
            .compose(RxHelper.handleResult())
            .compose(RxSchedulers.io_main())
    }

    override fun penpalListData(): Observable<List<UserBean>> {
        return mRepositoryManager.obtainRetrofitService(ApiService::class.java)
            .getPenpalList()
            .compose(RxHelper.handleResult())
            .compose(RxSchedulers.io_main())
    }



}