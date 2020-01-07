package com.stanny.nearpal.module.letter.mvp.contract

import com.frame.zxmvp.base.BasePresenter
import com.frame.zxmvp.base.IModel
import com.frame.zxmvp.base.IView
import rx.Observable

/**
 * Create By admin On 2017/7/11
 * 功能：
 */
interface LetterContract {
    //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
    interface View : IView {
        fun onDeleteResult()
    }

    //Model层定义接口,外部只需关心Model返回的数据,无需关心内部细节,即是否使用缓存
    interface Model : IModel {
        fun readLetterData(map: Map<String, String>) : Observable<Any>

        fun deleteLetterData(map: Map<String, String>) : Observable<Any>
    }

    //方法
    abstract class Presenter : BasePresenter<View, Model>() {
        abstract fun readLetter(map: Map<String, String>)

        abstract fun deleteLetter(map: Map<String, String>)
    }
}

