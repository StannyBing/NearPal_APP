package com.stanny.nearpal.module.letter.mvp.model

import com.frame.zxmvp.base.BaseModel
import com.frame.zxmvp.baserx.RxHelper
import com.frame.zxmvp.baserx.RxSchedulers
import com.stanny.nearpal.app.ApiService
import com.stanny.nearpal.module.letter.bean.LetterBean
import com.stanny.nearpal.module.letter.mvp.contract.PenpalDetailContract
import com.stanny.nearpal.module.system.bean.UserBean
import rx.Observable

/**
 * Create By admin On 2017/7/11
 * 功能：
 */
class PenpalDetailModel : BaseModel(), PenpalDetailContract.Model {
    override fun userdata(map: Map<String, String>): Observable<UserBean> {
        return mRepositoryManager.obtainRetrofitService(ApiService::class.java)
            .getUserInfo(map)
            .compose(RxHelper.handleResult())
            .compose(RxSchedulers.io_main())
    }

    override fun lettersData(map: Map<String, String>): Observable<List<LetterBean>> {
        return mRepositoryManager.obtainRetrofitService(ApiService::class.java)
            .getLettersWithId(map)
            .compose(RxHelper.handleResult())
            .compose(RxSchedulers.io_main())
    }

    override fun deletePenpal(map: Map<String, String>): Observable<Any> {
        return mRepositoryManager.obtainRetrofitService(ApiService::class.java)
            .deletePenpal(map)
            .compose(RxHelper.handleResult())
            .compose(RxSchedulers.io_main())
    }


}