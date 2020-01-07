package com.stanny.nearpal.base

import com.stanny.nearpal.app.ApiConfigModule
import com.stanny.nearpal.module.system.bean.UserBean
import com.zx.zxutils.util.ZXSharedPrefUtil

/**
 * Created by Xiangb on 2019/12/18.
 * 功能：
 */
object UserManager {

    var user: UserBean? = null
        set(value) {
            ZXSharedPrefUtil().putObject("m_userbean", value)
            field = value
        }
        get() {
            if (field == null) {
                return ZXSharedPrefUtil().getObject("m_userbean")
            } else {
                return field
            }
        }

    var userName: String = ""
        set(value) {
            ZXSharedPrefUtil().putString("m_username", value)
            field = value
        }
        get() {
            if (field.isEmpty()) {
                return ZXSharedPrefUtil().getString("m_username")
            } else {
                return field
            }
        }

    var passWord: String = ""
        set(value) {
            ZXSharedPrefUtil().putString("m_password", value)
            field = value
        }
        get() {
            if (field.isEmpty()) {
                return ZXSharedPrefUtil().getString("m_password")
            } else {
                return field
            }
        }

    //账户id，用于非用户密码方式注册账户
    var id: String = ""
        set(value) {
            ZXSharedPrefUtil().putString("m_id", value)
            field = value
        }
        get() {
            if (field.isEmpty()) {
                return ZXSharedPrefUtil().getString("m_id")
            } else {
                return field
            }
        }

    fun loginOut() {
        ApiConfigModule.COOKIE = ""
        passWord = ""
        id = ""
    }
}