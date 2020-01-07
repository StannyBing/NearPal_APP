package com.stanny.nearpal.module.letter.mvp.contract

import com.frame.zxmvp.base.BasePresenter
import com.frame.zxmvp.base.IModel
import com.frame.zxmvp.base.IView
import com.stanny.nearpal.module.letter.bean.LetterBean
import com.stanny.nearpal.module.system.bean.UserBean
import rx.Observable

/**
 * Create By admin On 2017/7/11
 * 功能：
 */
interface PenpalDetailContract {
    //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
    interface View : IView {
        fun onUserResult(userBean: UserBean)

        fun onLettersResult(letterList: List<LetterBean>)

        fun onPenPalDelete()
    }

    //Model层定义接口,外部只需关心Model返回的数据,无需关心内部细节,即是否使用缓存
    interface Model : IModel {
        fun userdata(map: Map<String, String>) : Observable<UserBean>

        fun lettersData(map: Map<String, String>): Observable<List<LetterBean>>

        fun deletePenpal(map: Map<String, String>): Observable<Any>
    }

    //方法
    abstract class Presenter : BasePresenter<View, Model>() {
        abstract fun getLetterWith(id : Int)

        abstract fun deletePenpal(map: Map<String, String>)
    }
}

