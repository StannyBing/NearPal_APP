package com.stanny.nearpal.module.system.bean

import java.io.Serializable

/**
 * Created by Xiangb on 2019/12/10.
 * 功能：
 */
data class UserBean(var username: String = "",
                    var id: Int = 0,
                    var nickname: String = "",
                    var birthday: String = "",
                    var logintype: Int = 0,//登录类别 0注册 1微信 2QQ 3微博 4 手机号 5游客
                    var sex: String = "",
                    var balance: Int = 0,
                    var headicon : Int = 0) : Serializable {
}