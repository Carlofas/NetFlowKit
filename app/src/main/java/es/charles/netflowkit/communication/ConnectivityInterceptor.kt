package es.charles.netflowkit.communication

import android.content.Context
import android.util.Log
import es.charles.netflowkit.communication.BaseRetrofit.mGson
import es.charles.netflowkit.utils.NetworkUtil
import es.charles.netflowkit.utils.TAG
import okhttp3.Interceptor
import okhttp3.Response
import retrofit2.Invocation
import java.io.IOException

class ConnectivityInterceptor(private val mContext: Context) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        if (!NetworkUtil.isOnline(mContext)) {
            throw NoInternetException()
        }
        val builder = chain.request().newBuilder()
        val method = chain.request().method()
        val llamada: String?
        val body: Any?
        try {
            if (method == "POST") {
                llamada = chain.request().tag(Invocation::class.java)?.arguments()?.get(0).toString()
                body = chain.request().tag(Invocation::class.java)?.arguments()?.get(1)?:""
                Log.w("$TAG REQUEST", "LLAMADA -> $llamada" +
                        "\nREQUEST BODY:\n${mGson.toJson(body)}")
            } else {
                llamada = chain.request().tag(Invocation::class.java)?.arguments()?.get(0).toString()
                Log.w("$TAG REQUEST", "LLAMADA -> $llamada")
            }
        } catch (_: Exception){


        }

        return chain.proceed(builder.build())
    }

}