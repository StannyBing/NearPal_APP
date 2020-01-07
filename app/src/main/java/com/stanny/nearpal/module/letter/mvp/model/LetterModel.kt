package com.stanny.nearpal.module.letter.mvp.model

import com.frame.zxmvp.base.BaseModel
import com.frame.zxmvp.baserx.RxHelper
import com.frame.zxmvp.baserx.RxSchedulers
import com.stanny.nearpal.app.ApiService
import com.stanny.nearpal.module.letter.mvp.contract.LetterContract
import rx.Observable

/**
 * Create By admin On 2017/7/11
 * 功能：
 */
class LetterModel : BaseModel(), LetterContract.Model {
    override fun readLetterData(map: Map<String, String>): Observable<Any> {
        return mRepositoryManager.obtainRetrofitService(ApiService::class.java)
            .readLetter(map)
            .compose(RxHelper.handleResult())
            .compose(RxSchedulers.io_main())
    }

    override fun deleteLetterData(map: Map<String, String>): Observable<Any> {
        return mRepositoryManager.obtainRetrofitService(ApiService::class.java)
            .deleteLetter(map)
            .compose(RxHelper.handleResult())
            .compose(RxSchedulers.io_main())
    }


}