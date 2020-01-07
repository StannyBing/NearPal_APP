package com.stanny.nearpal.module.system.mvp.contract

import com.frame.zxmvp.base.BasePresenter
import com.frame.zxmvp.base.IModel
import com.frame.zxmvp.base.IView
import com.stanny.nearpal.module.system.bean.UserBean
import okhttp3.RequestBody
import rx.Observable

/**
 * Create By admin On 2017/7/11
 * 功能：
 */
interface LoginContract {
    //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
    interface View : IView {
        fun onLoginResult(userBean: UserBean)

        fun onVisitRegisterResult(userBean: UserBean)
    }

    //Model层定义接口,外部只需关心Model返回的数据,无需关心内部细节,即是否使用缓存
    interface Model : IModel {
        fun loginData(body: RequestBody): Observable<UserBean>

        fun visitRegisterData(): Observable<UserBean>
    }

    //方法
    abstract class Presenter : BasePresenter<View, Model>() {
        abstract fun loginApp(body: RequestBody)

        abstract fun visitRegister()
    }
}

