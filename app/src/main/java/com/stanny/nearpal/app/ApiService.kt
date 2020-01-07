package com.stanny.nearpal.app

import com.frame.zxmvp.basebean.BaseRespose
import com.stanny.nearpal.module.letter.bean.LetterBean
import com.stanny.nearpal.module.system.bean.UserBean
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.QueryMap
import rx.Observable

/**
 * Created by Xiangb on 2019/2/26.
 * 功能：
 */
interface ApiService {

    @POST("user/loginByUsername")
    fun loginApp(@Body body: RequestBody): Observable<BaseRespose<UserBean>>

    @POST("user/registerByUsername")
    fun registerByUserName(@Body body: RequestBody): Observable<BaseRespose<UserBean>>

    @POST("user/modifyUserInfo")
    fun modifyUserInfo(@Body body: RequestBody): Observable<BaseRespose<UserBean>>

    @POST("user/registerByVisit")
    fun visitRegister(): Observable<BaseRespose<UserBean>>

    @POST("user/loginByVisit")
    fun visitLoginApp(@Body body: RequestBody): Observable<BaseRespose<UserBean>>

    @POST("letter/sendLetter")
    fun sendLetter(@Body body: RequestBody): Observable<BaseRespose<Any>>

    @GET("letter/getAcceptLetters")
    fun getAcceptLetters(): Observable<BaseRespose<List<LetterBean>>>

    @GET("penpal/getPenpalList")
    fun getPenpalList(): Observable<BaseRespose<List<UserBean>>>

    @GET("letter/getLettersWithId")
    fun getLettersWithId(@QueryMap map: Map<String, String>): Observable<BaseRespose<List<LetterBean>>>

    @POST("penpal/deletePenPal")
    fun deletePenpal(@QueryMap map: Map<String, String>): Observable<BaseRespose<Any>>

    @GET("user/getInfoById")
    fun getUserInfo(@QueryMap map: Map<String, String>): Observable<BaseRespose<UserBean>>

    @POST("letter/readLetter")
    fun readLetter(@QueryMap map: Map<String, String>): Observable<BaseRespose<Any>>

    @POST("letter/deleteLetter")
    fun deleteLetter(@QueryMap map: Map<String, String>): Observable<BaseRespose<Any>>
}