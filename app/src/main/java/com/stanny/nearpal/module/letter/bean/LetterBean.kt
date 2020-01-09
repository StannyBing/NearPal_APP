package com.stanny.nearpal.module.letter.bean

import java.io.Serializable

/**
 * Created by Xiangb on 2019/12/10.
 * 功能：
 */
data class LetterBean(
    var id: Int = 0,
    var sendtime: String = "",
    var accepttime: String = "",
    var postcode: String = "",
    var stampid: Int = 0,
    var mstatus: Int? = 0,//状态（0随机信件 1已发送 2已收到 3已拆封 4已删除）
    var letterinfo: String = "",
    var lettercall: String = "",
    var letterdetail: String = "",
    var israndom: Int = 0,//是否为随机信件 0否 1是
    var senduserid : Int = 0,
    var acceptuserid : Int? = 0,
    var nickname: String? = "",
    var headicon: Int? = 0
) : Serializable {
}