package com.stanny.nearpal.app

import android.app.Application
import android.content.Context
import com.frame.zxmvp.di.module.GlobalConfigModule
import com.frame.zxmvp.http.AppDelegate
import com.frame.zxmvp.http.GlobalHttpHandler
import com.frame.zxmvp.integration.ConfigModule
import com.frame.zxmvp.integration.IRepositoryManager
import com.zx.zxutils.util.ZXLogUtil
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.net.URLDecoder


/**
 * Created by Xiangb on 2019/2/26.
 * 功能：0
 */
class ApiConfigModule : ConfigModule {
    companion object {
        const val ISRELEASE = false        //正式发布 用true，平时内部使用可以用false，然后自己在正式环境测试环境之间切换，不用频繁打包
        var BASE_IP =
            if (ISRELEASE) "http://39.106.180.194:20000/nearpal/" else "http://192.168.11.32:20000/nearpal/"
        var COOKIE = ""

    }


    override fun applyOptions(context: Context, builder: GlobalConfigModule.Builder) {
        builder.baseurl(BASE_IP)
            //使用builder可以为框架配置一些配置信息
            .globalHttpHandler(object : GlobalHttpHandler {
                // 这里可以提供將於一个全局处理Http请求和响应结果的处理类,
                // 这里可以比客户端提前一步拿到服务器返回的结果,可以做一些操作,比如token超时,重新获取
                override fun onHttpResultResponse(
                    httpResult: String?,
                    chain: Interceptor.Chain,
                    response: Response
                ): Response {
                    /* 这里可以先客户端一步拿到每一次http请求的结果,可以解析成json,做一些操作,如检测到token过期后
                       重新请求token,并重新执行请求 */
                    var url = chain.request().url().toString()
                    if (url.contains(".do")) {
                        url = url.substring(0, url.indexOf(".do"))
                    }
                    ZXLogUtil.loge("Response : ${url.substring(url.lastIndexOf("/") + 1)} : $httpResult")
                    if (COOKIE.isEmpty()) {
//                            ZXSharedPrefUtil().putString("request_list", "")
                        if (response.headers("Set-Cookie").isNotEmpty()) {
                            val cookies = response.headers("Set-Cookie")[0].split(";")
                            if (cookies.isNotEmpty()) {
                                cookies.forEach {
                                    if (it.contains("JSESSIONID")) {
                                        COOKIE = it.replace("JSESSIONID=", "")
                                        return@forEach
                                    }
                                }
                            }
                        }
                    }
                    return response
                }

                // 这里可以在请求服务器之前可以拿到request,做一些操作比如给request统一添加token或者header以及参数加密等操作
                override fun onHttpRequestBefore(
                    chain: Interceptor.Chain,
                    request: Request
                ): Request {
                    // 如果需要再请求服务器之前做一些操作,则重新返回一个做过操作的的requeat如增加header,不做操作则直接返回request参数
                    ZXLogUtil.loge("Request : ${request.url()}")
                    return chain.request().newBuilder()
                        .url(URLDecoder.decode(request.url().toString())).header("cookie", COOKIE)
                        .build()
                }
            })
            .responseErroListener { context1, e ->
                /* 用来提供处理所有错误的监听
           rxjava必要要使用ErrorHandleSubscriber(默认实现Subscriber的onError方法),此监听才生效 */
            }
    }

    override fun registerComponents(context: Context, repositoryManager: IRepositoryManager) {
        repositoryManager.injectRetrofitService(ApiService::class.java)
    }

    override fun injectAppLifecycle(
        context: Context?,
        lifecycles: MutableList<AppDelegate.Lifecycle>
    ) {
        // AppDelegate.Lifecycle 的所有方法都会在基类Application对应的生命周期中被调用,所以在对应的方法中可以扩展一些自己需要的逻辑
        lifecycles.add(object : AppDelegate.Lifecycle {
            override fun onCreate(application: Application) {

            }

            override fun onTerminate(application: Application) {

            }
        })
    }


}