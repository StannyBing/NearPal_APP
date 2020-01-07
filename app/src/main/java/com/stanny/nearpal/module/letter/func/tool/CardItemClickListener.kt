package com.stanny.nearpal.module.letter.func.tool

import com.stanny.nearpal.module.letter.bean.LetterBean

/**
 * Created by Xiangb on 2017/11/30.
 * 功能：
 */
interface CardItemClickListener {
    fun onItemClick(letterResult: LetterBean?, position: Int)
    fun onItemLongClick(letterResult: LetterBean?, position: Int)
}